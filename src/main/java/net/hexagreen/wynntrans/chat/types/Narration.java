package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class Narration extends WynnChatText {
    protected final String pKeyNarration;

    public Narration(Text text, Pattern regex) {
        super(text, regex);
        String hash = DigestUtils.sha1Hex(text.getString());
        this.pKeyNarration = parentKey + hash;
    }

    @Override
    protected String setParentKey() {
        return rootKey + "narration.";
    }

    @Override
    protected void build() {
        if(inputText.getSiblings().isEmpty()) {
            if(WTS.checkTranslationExist(pKeyNarration, getContentString())) {
                resultText = newTranslate(pKeyNarration).setStyle(getStyle());
            }
            else {
                resultText = inputText;
            }
        }
        else {
            String keyContent = pKeyNarration + "_1";
            String valContent = getContentString();
            if(WTS.checkTranslationExist(keyContent, valContent)) {
                resultText = newTranslate(keyContent).setStyle(getStyle());
            }
            else {
                resultText = MutableText.of(inputText.getContent()).setStyle(getStyle());
            }
            for(int index = 0; inputText.getSiblings().size() > index; index++) {
                String keySibling = pKeyNarration + "_" + (index + 2);
                String valSibling = getContentString(index);
                if(WTS.checkTranslationExist(keySibling, valSibling)) {
                    resultText.append(newTranslate(keySibling).setStyle(getStyle(index)));
                }
                else {
                    resultText.append(inputText.getSiblings().get(index));
                }
            }
        }
    }
}
