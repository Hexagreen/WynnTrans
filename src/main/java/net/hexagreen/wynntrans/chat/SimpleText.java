package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class SimpleText extends WynnChatText {
    private static boolean translationRegisterControl = true;
    private final String keyText;
    private final String valText;

    public SimpleText(Text text, Pattern regex) {
        super(text, regex);
        this.valText = inputText.getString();
        this.keyText = parentKey + DigestUtils.sha1Hex(valText);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "normalText.";
    }

    @Override
    protected void build() {
        if(valText.isEmpty()) {
            resultText = inputText;
            return;
        }

        if(inputText.getSiblings().size() > 1) {
            if(translationRegisterControl) {
                debugClass.writeString2File(inputText.getString(), "getString.txt", "Simple");
                debugClass.writeTextAsJSON(inputText, "UnregisteredSimpleText");
            }
            resultText = inputText;
            return;
        }

        if(inputText.getSiblings().size() == 1) {
            resultText = Text.empty().setStyle(getStyle());
            if(checkTranslationExistWithControl(keyText, valText)) {
                resultText.append(newTranslate(keyText).setStyle(getStyle(0)));
            }
            else {
                resultText.append(getSibling(0));
            }
        }
        else {
            if(checkTranslationExistWithControl(keyText, getContentString())) {
                resultText = newTranslate(keyText);
            }
            else {
                resultText = MutableText.of(inputText.getContent());
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
