package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class Info extends WynnChatText {
    private final String hash;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\[Info] ").matcher(text.getString()).find();
    }

    public Info(Text text) {
        super(text);
        this.hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "info";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey).setStyle(getStyle(0)));

        for(int index = 1; getSiblings().size() > index; index++) {
            String keySibling = translationKey + "." + hash + "." + index;
            String valSibling = getContentString(index);

            if(valSibling.isEmpty()) {
                resultText.append("");
                continue;
            }
            if(valSibling.contains("wynn.gg") || valSibling.contains("wynncraft.com")) {
                resultText.append(getSibling(index));
                continue;
            }
            switch(valSibling) {
                case "For more information, visit " -> {
                    resultText.append(Text.translatable(translationKey + ".moreInfo").setStyle(getStyle(index)));
                    continue;
                }
                case "Visit " -> {
                    resultText.append(Text.translatable(translationKey + ".moreInfo_alt1").setStyle(getStyle(index)));
                    continue;
                }
                case " for more!" -> {
                    resultText.append(Text.translatable(translationKey + ".moreInfo_alt2").setStyle(getStyle(index)));
                    continue;
                }
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
