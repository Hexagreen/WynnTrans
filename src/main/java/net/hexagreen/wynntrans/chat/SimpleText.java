package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class SimpleText extends WynnChatText {
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
            debugClass.writeString2File(inputText.getString(), "getString.txt", "Simple");
            debugClass.writeString2File(inputText.toString(), "toString.txt", "Simple");
            debugClass.writeTextAsJSON(inputText);
            resultText = inputText;
            return;
        }

        if(inputText.getSiblings().size() == 1) {
            resultText = Text.empty().setStyle(getStyle());
            if(WTS.checkTranslationExist(keyText, valText)) {
                resultText.append(newTranslate(keyText).setStyle(getStyle(0)));
            }
            else {
                resultText.append(getSibling(0));
            }
        }
        else {
            if(WTS.checkTranslationExist(keyText, getContentString())) {
                resultText = newTranslate(keyText);
            }
            else {
                resultText = MutableText.of(inputText.getContent());
            }
        }

    }
}
