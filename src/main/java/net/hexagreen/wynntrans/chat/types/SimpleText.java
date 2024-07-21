package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class SimpleText extends WynnChatText {
    private static boolean translationRegisterControl = true;
    private final String keyText;
    private final String valText;
    private final Style textStyle;

    public SimpleText(Text text, Pattern regex) {
        super(text, regex);
        if(inputText.getSiblings().isEmpty()) {
            this.valText = inputText.getString().replaceFirst("^(?:§.)+", "");
            String styleCode = inputText.getString().replace(valText, "");
            this.textStyle = parseStyleCode(styleCode);
        }
        else {
            this.valText = inputText.getString();
            this.textStyle = Style.EMPTY;
        }
        this.keyText = parentKey + DigestUtils.sha1Hex(valText);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "normalText.";
    }

    @Override
    protected void build() {
        if(valText.isEmpty() /*|| valText.equals(" ")*/) {
            resultText = inputText;
            return;
        }

        if(!inputText.getSiblings().isEmpty()) {
            int i = 1;
            if(!inputText.getContent().equals(PlainTextContent.EMPTY)) {
                String valContentText = ((PlainTextContent) inputText.getContent()).string();
                String keyContentText = this.keyText + "_0";
                if (checkTranslationExistWithControl(keyContentText, valContentText)) {
                    resultText = newTranslate(keyContentText).setStyle(getStyle());
                } else {
                    resultText = Text.literal(valContentText).setStyle(getStyle());
                    if(translationRegisterControl) {
                        debugClass.writeString2File(inputText.getString(), "getString.txt", "Simple");
                        debugClass.writeTextAsJSON(inputText, "UnregisteredSimpleText");
                    }
                }
            }

            for(Text sibling : inputText.getSiblings()) {
                String valText = sibling.getString();
                String keyText = this.keyText + "_" + i++;

                if(resultText == null) resultText = Text.empty().setStyle(getStyle());
                if(checkTranslationExistWithControl(keyText, valText)) {
                    resultText.append(newTranslate(keyText).setStyle(sibling.getStyle()));
                }
                else {
                    resultText.append(sibling);
                }
            }
        }
        else {
            if(checkTranslationExistWithControl(keyText, valText)) {
                resultText = newTranslate(keyText).setStyle(textStyle);
            }
            else {
                resultText = inputText;
                if(translationRegisterControl) {
                    debugClass.writeString2File(inputText.getString(), "literal.txt");
                    debugClass.writeTextAsJSON(inputText, "Literal");
                }
            }
        }
    }

    public static void setTranslationControl(boolean control) {
        translationRegisterControl = control;
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
