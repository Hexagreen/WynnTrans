package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class LittleInform extends WynnChatText {
    private final String keyInform;

    public LittleInform(Text text, Pattern regex) {
        super(text, regex);
        this.keyInform = DigestUtils.sha1Hex(inputText.getString()).substring(0, 12);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "inform.";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(0)).append(getSibling(1)).append(getSibling(2));

        for(int i = 3; i < inputText.getSiblings().size(); i++) {
            String valInform = getSibling(i).getString();

            if(WTS.checkTranslationExist(parentKey + keyInform + "_" + (i - 2), valInform)) {
                resultText.append(newTranslate(parentKey + keyInform + "_" + (i - 2)).setStyle(getStyle(i)));
            }
            else {
                resultText.append(getSibling(i));
            }
        }
    }
}
