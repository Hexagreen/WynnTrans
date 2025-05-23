package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

public class SimpleText extends WynnChatText {
    private static boolean translationRegisterControl = true;
    private final String keyText;
    private final String valText;
    private final Style textStyle;

    public static void setTranslationControl(boolean control) {
        translationRegisterControl = control;
    }

    public SimpleText(Text text) {
        super(text);
        if(getSiblings().isEmpty()) {
            this.valText = inputText.getString().replaceFirst("^(?:§.)+", "");
            String styleCode = inputText.getString().replace(valText, "");
            this.textStyle = parseStyleCode(styleCode);
        }
        else {
            this.valText = inputText.getString();
            this.textStyle = Style.EMPTY;
        }
        this.keyText = translationKey + DigestUtils.sha1Hex(valText);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "text.";
    }

    @Override
    protected void build() {
        if(valText.isEmpty() /*|| valText.equals(" ")*/) {
            resultText = inputText;
            return;
        }

        boolean recorded = false;
        if(!getSiblings().isEmpty()) {
            int i = 1;
            if(!inputText.getContent().equals(PlainTextContent.EMPTY)) {
                String valContentText = ((PlainTextContent) inputText.getContent()).string();
                String keyContentText = this.keyText + ".0";
                if(checkTranslationExistWithControl(keyContentText, valContentText)) {
                    resultText = Text.translatable(keyContentText).setStyle(getStyle());
                }
                else {
                    resultText = Text.literal(valContentText).setStyle(getStyle());
                    if(translationRegisterControl) {
                        debugClass.writeTextAsJSON(inputText, "UnregisteredSimpleText");
                        recorded = true;
                    }
                }
            }

            for(Text sibling : getSiblings()) {
                String valText = sibling.getString();
                String keyText = this.keyText + "." + i++;

                if(resultText == null) resultText = Text.empty().setStyle(getStyle());
                if(valText.equals("\n")) {
                    resultText.append("\n");
                    continue;
                }

                if(checkTranslationExistWithControl(keyText, valText)) {
                    resultText.append(Text.translatable(keyText).setStyle(sibling.getStyle()));
                }
                else {
                    resultText.append(sibling);
                    if(translationRegisterControl && !recorded) {
                        debugClass.writeTextAsJSON(inputText, "UnregisteredSimpleText");
                        recorded = true;
                    }
                }
            }
        }
        else {
            if(checkTranslationExistWithControl(keyText, valText)) {
                resultText = Text.translatable(keyText).setStyle(textStyle);
            }
            else {
                resultText = inputText;
                if(translationRegisterControl) {
                    debugClass.writeTextAsJSON(inputText, "Literal");
                }
            }
        }
    }

    private boolean checkTranslationExistWithControl(String key, String value) {
        if(translationRegisterControl) {
            return WTS.checkTranslationExist(key, value);
        }
        else {
            return WTS.checkTranslationDoNotRegister(key);
        }
    }
}
