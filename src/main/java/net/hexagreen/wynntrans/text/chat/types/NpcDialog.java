package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.TextNormalizer;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class NpcDialog extends WynnChatText {
    protected final String keyDialog;
    private final String keyName;
    private final String valName;
    private final NpcDialogNormalizer carrier;
    private boolean addiction = true;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\n?\\[(\\d+)/(\\d+)] .+:").matcher(text.getString()).find();
    }

    public NpcDialog(Text text) {
        this(new NpcDialogNormalizer(text));
    }

    private NpcDialog(NpcDialogNormalizer carrier) {
        super(carrier.getText());
        this.carrier = carrier;

        this.valName = getContentString(0).replace(": ", "");
        String npcName = normalizeStringForKey(valName);
        this.keyName = translationKey + "name." + npcName;
        String[] dialogCounter = getContentString().trim().replace("[", "").replace("]", "").split("/");
        String hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
        this.keyDialog = translationKey + "dialog." + npcName + "." + dialogCounter[1] + "." + dialogCounter[0] + "." + hash;
    }

    public NpcDialog setNoTranslationAddiction() {
        this.addiction = false;
        return this;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey;
    }

    @Override
    protected void build() {
        // Dialog counter
        resultText = Text.literal(getContentString()).setStyle(getStyle());

        // Talker name
        Text t0 = getSibling(0);
        if(checkTranslationExist(keyName, valName)) {
            resultText.append(Text.translatable(keyName).setStyle(t0.getStyle()));
            resultText.append(Text.literal(": ").setStyle(t0.getStyle()));
        }
        else {
            resultText.append(t0);
        }

        // Dialog body
        Text dialogBody = getSibling(1);
        List<Text> dialogArgs = carrier.getArgs(keyDialog, this::checkTranslationExist);
        if(checkTranslationExist(keyDialog, dialogBody.getString())) {
            resultText.append(Text.translatable(keyDialog, dialogArgs.toArray(new Object[0])).setStyle(dialogBody.getStyle()));
        }
        else {
            MutableText reassembled = Text.empty().setStyle(dialogBody.getStyle());
            String[] strings = (dialogBody.getString() + "%sEOL").split("%s");
            for(int j = 0, l = strings.length - 1; j < l; j++) {
                if(!strings[j].isEmpty()) reassembled.append(strings[j]);
                if(j != l - 1) reassembled.append(dialogArgs.get(j));
            }
            resultText.append(reassembled);
        }
    }

    private boolean checkTranslationExist(String key, String val) {
        if(addiction) return WTS.checkTranslationExist(key, val);
        else return WTS.checkTranslationDoNotRegister(key);
    }

    private static class NpcDialogNormalizer extends TextNormalizer {

        protected NpcDialogNormalizer(Text text) {
            super(text, WTS.getRulebooks().npcDialogRulebook);
        }

        @Override
        protected void normalizer(Text text) {
            MutableText result;

            // Dialog counter
            if(text.getContent().equals(PlainTextContent.EMPTY)) {
                result = copiedSiblings.getFirst().copy();
                copiedSiblings.removeFirst();
            }
            else {
                result = text.copyContentOnly().setStyle(text.getStyle());
            }

            // Talker name
            Text talker = copiedSiblings.removeFirst();
            result.append(talker);

            // Dialog body
            if(copiedSiblings.size() == 1 && hasNotMatchWithRule(copiedSiblings.getFirst().getString())) {
                this.text = result.append(copiedSiblings.getFirst());
                this.args = new ArrayList<>();
                this.flags = new ArrayList<>();
            }
            else {
                Style desiredStyle = DialogColor.findDialogStyle(talker.getStyle());
                ArgsRecord argsRecord = siblingsToArgs(copiedSiblings, desiredStyle);

                this.text = result.append(Text.literal(argsRecord.textContent()).setStyle(desiredStyle));
                this.args = argsRecord.args();
                this.flags = argsRecord.flags();
            }
        }

        private enum DialogColor {
            BASIC(Style.EMPTY.withColor(Formatting.DARK_GREEN), Style.EMPTY.withColor(Formatting.GREEN)),
            FES_HERO(Style.EMPTY.withColor(Formatting.DARK_PURPLE), Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)),
            FES_BONFIRE(Style.EMPTY.withColor(Formatting.GOLD), Style.EMPTY.withColor(Formatting.YELLOW)),
            FES_SPIRITS(Style.EMPTY.withColor(Formatting.DARK_GRAY), Style.EMPTY.withColor(Formatting.GRAY)),
            FES_BLIZZARD(Style.EMPTY.withColor(Formatting.DARK_AQUA), Style.EMPTY.withColor(Formatting.AQUA));

            private final Style nameColor;
            private final Style dialogColor;

            static Style findDialogStyle(Style nameStyle) {
                return Arrays.stream(DialogColor.values())
                        .filter(type -> Objects.equals(type.nameColor.getColor(), nameStyle.getColor()))
                        .findFirst().orElse(BASIC).dialogColor;
            }

            DialogColor(Style nameColor, Style dialogColor) {
                this.nameColor = nameColor;
                this.dialogColor = dialogColor;
            }
        }
    }
}
