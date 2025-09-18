package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.TextNormalizer;
import net.hexagreen.wynntrans.text.display.ProxySimpleDisplay;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleDisplay extends WynnDisplayText {
    private final DisplayCarrier carrier;
    private final String valText;
    private final Style styleText;
    private String keyText;

    public static Text translateTextTree(Text text, String translationKey) {
        return new SimpleDisplay(text).changeKey(translationKey).text();
    }

    private static boolean blankChecker(String str) {
        return str.replaceAll("§.|\\n|À|[\uE000-\uEAFF]|[\uDAFF\uDC00-\uDB01\uDC00]", "").isBlank();
    }

    public SimpleDisplay(Text text) {
        this(new DisplayCarrier(text));
    }

    private SimpleDisplay(DisplayCarrier carrier) {
        super(carrier.getText());
        this.carrier = carrier;
        this.valText = initValText();
        this.keyText = translationKey + DigestUtils.sha1Hex(valText);
        this.styleText = parseStyleCode(inputText.getString()).withParent(getStyle());
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(blankChecker(valText)) {
            resultText = inputText;
            return;
        }
        Text _val = Text.literal(valText).setStyle(styleText);
        MutableText itemName = new ItemName(_val).returnNullMode().textAsMutable();
        if(itemName != null) {
            resultText = itemName;
            return;
        }

        List<Text> displayArgs = carrier.getArgs(keyText, WTS::checkTranslationExist);
        if(WTS.checkTranslationExist(keyText, valText)) {
            resultText = newTranslate(keyText, displayArgs.toArray(Object[]::new)).setStyle(styleText);
        }
        else {
            if(this instanceof ProxySimpleDisplay) {
                resultText = carrier.originalText.copy();
                return;
            }
            MutableText reassembled = Text.empty().setStyle(styleText);
            String[] strings = valText.split("%s", -1);
            for(int j = 0, l = strings.length; j < l; j++) {
                if(!strings[j].isEmpty()) reassembled.append(strings[j]);
                if(j != l - 1) reassembled.append(displayArgs.removeFirst());
            }
            resultText = reassembled;
            if(!carrier.timerTranslated) debugClass.writeTextAsJSON(carrier.originalText, "Display");
        }
    }

    private SimpleDisplay changeKey(String newKey) {
        this.keyText = newKey;
        return this;
    }

    protected String initValText() {
        return inputText.getString().replaceFirst("^(?:§.)+", "");
    }

    protected MutableText newTranslate(String key, Object... args) {
        return Text.translatable(key, args);
    }

    private static class DisplayCarrier extends TextNormalizer {
        boolean timerTranslated;

        protected DisplayCarrier(Text text) {
            super(text, WTS.getRulebooks().displayRuleBook);
        }

        @Override
        protected ArgFilterResult argsFilter(Text sibling) {
            if(!sibling.getStyle().getFont().equals(Identifier.of("minecraft:default")))
                return argFilterResultOf(sibling, true);
            Matcher m;
            if((m = ITime.timePattern.matcher(sibling.getString())).find() && !m.group().isBlank()) {
                Text timer = ITime.translateTime(m.group());
                if(!m.replaceFirst("").isBlank()) return null;
                String replaced = m.replaceFirst(timer.getString());
                timerTranslated = true;
                return argFilterResultOf(Text.literal(replaced).setStyle(sibling.getStyle()), true);
            }
            Matcher discount;
            if((discount = Pattern.compile("(\\d+%) OFF").matcher(sibling.getString())).find()) {
                String rate = discount.group(1);
                return argFilterResultOf(Text.translatable("wytr.display.discountRate", rate).setStyle(sibling.getStyle()), true);
            }
            return null;
        }

        @Override
        protected void normalize(Text text) {
            if(blankChecker(text.getString())) {
                this.text = text;
                this.args = new ArrayList<>();
                this.flags = new ArrayList<>();
                return;
            }

            Deque<Text> visited = new ArrayDeque<>();
            Style[] styles = {Style.EMPTY, Style.EMPTY};
            Identifier defaultFont = Identifier.of("minecraft:default");
            text.visit((s, t) -> {
                visited.add(Text.literal(t).setStyle(s));
                if(blankChecker(t) || !s.getFont().equals(defaultFont)) return Optional.empty();
                if(styles[0] == Style.EMPTY) {
                    styles[0] = s;
                }
                if(Objects.equals(GRAY.getColor(), s.getColor())) {
                    styles[1] = s;
                }

                return Optional.empty();
            }, Style.EMPTY);

            copiedSiblings = visited;
            Style desiredStyle = styles[1] != Style.EMPTY ? styles[1] : styles[0];
            ArgsRecord argsRecord = siblingsToArgs(copiedSiblings, desiredStyle);

            this.text = Text.literal(argsRecord.textContent()).setStyle(desiredStyle);
            this.args = argsRecord.args();
            this.flags = argsRecord.flags();
        }
    }
}
