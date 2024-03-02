package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class Info extends WynnChatText {
    private final String hash;

    public Info(Text text, Pattern regex) {
        super(text, regex);
        this.hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "info";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)));

        for (int index = 1; inputText.getSiblings().size() > index; index++) {
            String keySibling = parentKey + "." + hash + "_" + index;
            String valSibling = getContentString(index);

            if(valSibling.isEmpty()) {
                resultText.append("");
                continue;
            }
            if(valSibling.contains("wynncraft.com")) {
                resultText.append(getSibling(index));
                continue;
            }
            if(valSibling.equals("For more information, visit ")) {
                resultText.append(newTranslate(parentKey + ".moreInfo").setStyle(getStyle(index)));
                continue;
            }
            if(valSibling.equals("Visit ")) {
                resultText.append(newTranslate(parentKey + ".moreInfo_alt1").setStyle(getStyle(index)));
                continue;
            }
            if(valSibling.equals(" for more!")) {
                resultText.append(newTranslate(parentKey + ".moreInfo_alt2").setStyle(getStyle(index)));
                continue;
            }

            if (WTS.checkTranslationExist(keySibling, valSibling)) {
                resultText.append(newTranslate(keySibling).setStyle(getStyle(index)));
            }
            else {
                resultText.append(getSibling(index));
            }
        }
    }
}
