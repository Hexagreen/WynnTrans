package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnSystemText;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class SimpleSystemText extends WynnSystemText {
    private static boolean translationRegisterControl = true;
    private final String keyText;
    private final String valText;

    public SimpleSystemText(Text text, Pattern ignore) {
        super(text, null);
        this.valText = lineFeedReplacer(inputText.getString());
        this.keyText = parentKey + DigestUtils.sha1Hex(lineFeedRemover(inputText.getString()));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "normalText.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, UnprocessedChatTypeException {
        if(valText.isEmpty() || valText.equals(" ")) {
            resultText = originText.copy();
            return;
        }

        if(inputText.getSiblings().size() > 1) {
            int i = 1;
            for(Text sibling : inputText.getSiblings()) {
                String valText = lineFeedReplacer(sibling.getString());
                String keyText = this.keyText + "_" + i++;

                if(checkTranslationExistWithControl(keyText, valText)) {
                    if(resultText == null) resultText = Text.empty().setStyle(getStyle()).append(header);
                    resultText.append(newTranslate(keyText, splitter).setStyle(sibling.getStyle()));
                }
                else {
                    if(resultText == null) resultText = originText.copy();
                    debugClass.writeTextAsJSON(inputText, "SystemText");
                }
            }
        }
        else {
            if(resultText == null) resultText = Text.empty().setStyle(getStyle()).append(header);
            if(checkTranslationExistWithControl(keyText, valText)) {
                resultText.append(newTranslate(keyText, splitter).setStyle(getStyle()));
            }
            else {
                resultText = originText.copy();
                if(translationRegisterControl) {
                    debugClass.writeTextAsJSON(inputText, "SystemLiteral");
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
