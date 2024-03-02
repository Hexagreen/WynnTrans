package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class GuideAlert extends WynnChatText {
    private final String pKeyGnA;

    public GuideAlert(Text text, Pattern regex) {
        super(text, regex);
        String hash = DigestUtils.sha1Hex(text.getString());
        this.pKeyGnA = parentKey + hash;
    }

    @Override
    protected String setParentKey() {
        return rootKey + "guideAlert.";
    }

    @Override
    protected void build() {
        resultText = MutableText.of(inputText.getContent()).setStyle(inputText.getStyle())
                .append(getSibling(0))
                .append(getSibling(1));
        for(int index = 2; inputText.getSiblings().size() > index; index++) {
            String keySibling = pKeyGnA + "_" + (index - 1);
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
