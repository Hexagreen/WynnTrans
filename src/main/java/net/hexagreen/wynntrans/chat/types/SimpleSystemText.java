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
        this.valText = initValText();
        this.keyText = initKeyText();
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

        boolean recorded = false;
        if(inputText.getSiblings().size() > 1) {
            int i = 1;
            for(Text sibling : inputText.getSiblings()) {
                String valText = lineFeedReplacer(sibling.getString());
                String keyText = this.keyText + "_" + i++;

                if(checkTranslationExistWithControl(keyText, valText)) {
                    if(resultText == null) resultText = Text.empty().setStyle(getStyle()).append(header);
                    resultText.append(newTranslateWithSplit(keyText).setStyle(sibling.getStyle()));
                }
                else {
                    if(resultText == null) resultText = originText.copy();
                    if(translationRegisterControl && !recorded){
                        debugClass.writeTextAsJSON(inputText, "SystemText");
                        recorded = true;
                    }
                }
            }
        }
        else {
            if(resultText == null) resultText = Text.empty().setStyle(getStyle()).append(header);
            if(checkTranslationExistWithControl(keyText, valText)) {
                resultText.append(newTranslateWithSplit(keyText).setStyle(getStyle(0)));
            }
            else {
                resultText = originText.copy();
                if(translationRegisterControl) {
                    debugClass.writeTextAsJSON(inputText, "SystemLiteral");
                }
            }
        }
    }

    protected String initValText() {
        return lineFeedReplacer(inputText.getString());
    }

    private String initKeyText() {
        return parentKey + DigestUtils.sha1Hex(replacerRemover(valText));
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
