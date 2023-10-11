package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class Narration extends WynnChatText {
    private final String pKeyNarration;

    Narration(Text text, Pattern regex) {
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
        if(inputText.getSiblings().size() == 0) {
            if(WTS.checkTranslationExist(pKeyNarration, getContentLiteral())) {
                resultText = newTranslate(pKeyNarration);
            }
        }
        else {
            String keyContent = pKeyNarration + "_1";
            String valContent = getContentLiteral();
            if(WTS.checkTranslationExist(keyContent, valContent)) {
                resultText = newTranslate(keyContent);
            }
            for(int index = 0; inputText.getSiblings().size() > index; index++) {
                String keySibling = pKeyNarration + "_" + (index + 2);
                String valSibling = getContentLiteral(index);
                if(WTS.checkTranslationExist(keySibling, valSibling)) {
                    resultText.append(newTranslate(keySibling));
                }
            }
        }
    }
}
