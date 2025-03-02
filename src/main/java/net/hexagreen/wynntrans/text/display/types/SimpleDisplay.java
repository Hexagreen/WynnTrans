package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.chat.TextNormalizer;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class SimpleDisplay extends WynnDisplayText {
    private final DisplayCarrier carrier;
    private final String valText;
    private final Style styleText;
    private String keyText;

    public static Text translateTextTree(Text text, String translationKey) {
        return new SimpleDisplay(text).changeKey(translationKey).text();
    }

    private static boolean blankChecker(String str) {
        return str.replaceAll("(§.|\\n|À)", "").isBlank();
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

        List<Text> displayArgs = carrier.getArgs(keyText, WTS::checkTranslationExist);
        if(WTS.checkTranslationExist(keyText, valText)) {
            resultText = newTranslate(keyText, displayArgs.toArray(Object[]::new)).setStyle(styleText);
        }
        else {
            MutableText reassembled = Text.empty().setStyle(styleText);
            String[] strings = valText.split("%s", -1);
            for(int j = 0, l = strings.length; j < l; j++) {
                if(!strings[j].isEmpty()) reassembled.append(strings[j]);
                if(j != l - 1) reassembled.append(displayArgs.removeFirst());
            }
            resultText = reassembled;
            debugClass.writeTextAsJSON(carrier.originalText, "Display");
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

        protected DisplayCarrier(Text text) {
            super(text, WTS.getRulebooks().displayRuleBook);
        }

        @Override
        protected boolean argsFilter(Text sibling) {
            return !sibling.getStyle().getFont().equals(Identifier.of("minecraft:default"));
        }

        @Override
        protected void normalize(Text text) {
            if(blankChecker(text.getString())) {
                this.text = text;
                this.args = new ArrayList<>();
                this.flags = new ArrayList<>();
            }

            List<Text> visited = new ArrayList<>();
            Style[] styles = {Style.EMPTY, Style.EMPTY};
            text.visit((s, t) -> {
                visited.add(Text.literal(t).setStyle(s));
                if(!blankChecker(t) && styles[0] == Style.EMPTY) {
                    styles[0] = s;
                }
                if(!blankChecker(t) && Objects.equals(TextColor.fromFormatting(Formatting.GRAY), s.getColor())) {
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
