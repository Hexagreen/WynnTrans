package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

public class Narration extends WynnChatText {
    protected final String pKeyNarration;
    private boolean addiction = true;

    public Narration(Text text) {
        super(text);
        String hash = DigestUtils.sha1Hex(text.getString());
        this.pKeyNarration = parentKey + hash;
    }

    public Narration setNoTranslationAddiction() {
        this.addiction = false;
        return this;
    }

    @Override
    protected String setParentKey() {
        return rootKey + "narration.";
    }

    @Override
    protected void build() {
        if(getSiblings().isEmpty()) {
            if(checkTranslationExist(pKeyNarration, getContentString())) {
                resultText = Text.translatable(pKeyNarration).setStyle(getStyle());
            }
            else {
                resultText = inputText;
            }
        }
        else {
            String keyContent = pKeyNarration + "_1";
            String valContent = getContentString();
            if(checkTranslationExist(keyContent, valContent)) {
                resultText = Text.translatable(keyContent).setStyle(getStyle());
            }
            else {
                resultText = MutableText.of(inputText.getContent()).setStyle(getStyle());
            }
            for(int index = 0; getSiblings().size() > index; index++) {
                String keySibling = pKeyNarration + "_" + (index + 2);
                String valSibling = getContentString(index);
                if(checkTranslationExist(keySibling, valSibling)) {
                    resultText.append(Text.translatable(keySibling).setStyle(getStyle(index)));
                }
                else {
                    resultText.append(getSiblings().get(index));
                }
            }
        }
    }

    private boolean checkTranslationExist(String key, String val) {
        if(addiction) return WTS.checkTranslationExist(key, val);
        else return WTS.checkTranslationDoNotRegister(key);
    }
}
