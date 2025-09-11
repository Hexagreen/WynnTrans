package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class LittleInform extends WynnChatText {
    private final String keyInform;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(?:§.)?\\[(?:§.)?!(?:§.)?] .+").matcher(text.getString()).find();
    }

    public LittleInform(Text text) {
        super(colorCodedToStyled(text));
        this.keyInform = DigestUtils.sha1Hex(inputText.getString()).substring(0, 12);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "inform.";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(getStyle())
                .append(getSibling(0)).append(getSibling(1)).append(getSibling(2));
        for(int i = 3; i < getSiblings().size(); i++) {
            String valInform = getSibling(i).getString();
            if(i == 3 && valInform.matches("^ .+")) {
                resultText.append(" ");
                valInform = valInform.replaceFirst("^ ", "");
            }

            if(WTS.checkTranslationExist(translationKey + keyInform + "." + (i - 2), valInform)) {
                resultText.append(Text.translatable(translationKey + keyInform + "." + (i - 2)).setStyle(getStyle(i)));
            }
            else {
                resultText.append(getSibling(i));
            }
        }
    }
}
