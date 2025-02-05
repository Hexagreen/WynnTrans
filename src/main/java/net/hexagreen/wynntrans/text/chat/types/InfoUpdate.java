package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class InfoUpdate extends WynnChatText {
    private final String hash;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\[Major Update] ").matcher(text.getString()).find();
    }

    public InfoUpdate(Text text) {
        super(text);
        this.hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "updateInfo";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey).setStyle(getStyle(0)));

        for(int index = 1; getSiblings().size() > index; index++) {
            String keySibling = translationKey + "." + hash + "." + (index - 1);
            String valSibling = getContentString(index);
            if(valSibling.matches(".+\\..+/.+") || valSibling.isEmpty() || valSibling.equals(" ")) {
                resultText.append(getSibling(index));
                continue;
            }
            if(WTS.checkTranslationExist(keySibling, valSibling)) {
                resultText.append(Text.translatable(keySibling).setStyle(getStyle(index)));
            }
            else {
                resultText.append(getSibling(index));
            }
        }
    }
}
