package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

public class GuideAlert extends WynnChatText {
    private final String pKeyGnA;

    public GuideAlert(Text text) {
        super(text);
        String hash = DigestUtils.sha1Hex(text.getString());
        this.pKeyGnA = parentKey + hash;
    }

    @Override
    protected String setParentKey() {
        return rootKey + "guideAlert.";
    }

    @Override
    protected void build() {
        resultText = MutableText.of(inputText.getContent()).setStyle(inputText.getStyle()).append(getSibling(0)).append(getSibling(1));
        for(int index = 2; getSiblings().size() > index; index++) {
            String keySibling = pKeyGnA + "_" + (index - 1);
            String valSibling = getContentString(index);
            if(WTS.checkTranslationExist(keySibling, valSibling)) {
                resultText.append(Text.translatable(keySibling).setStyle(getStyle(index)));
            }
            else {
                resultText.append(getSiblings().get(index));
            }
        }
    }
}
