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
        super(text);
        this.keyInform = DigestUtils.sha1Hex(inputText.getString()).substring(0, 12);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "inform.";
    }

    @Override
    protected void build() {
        if(getSiblings().isEmpty()) {
            resultText = Text.empty();
            resultText.append(getHeader());
            String valInform = getContentString().replaceAll("^§.\\[§.!§.] ", "");
            if(WTS.checkTranslationExist(translationKey + keyInform, valInform)) {
                resultText.append(Text.translatable(translationKey + keyInform));
            }
            else {
                resultText.append(valInform);
            }
        }
        else {
            resultText = inputText.copyContentOnly().setStyle(getStyle())
                    .append(getSibling(0)).append(getSibling(1));
            for(int i = 2; i < getSiblings().size(); i++) {
                String valInform = getSibling(i).getString();

                if(WTS.checkTranslationExist(translationKey + keyInform + "." + (i - 1), valInform)) {
                    resultText.append(Text.translatable(translationKey + keyInform + "." + (i - 1)).setStyle(getStyle(i)));
                }
                else {
                    resultText.append(getSibling(i));
                }
            }
        }
    }

    private Text getHeader() {
        String head = getContentString().substring(0, 10).split(" ")[0];
        return Text.literal(head).append(" ");
    }
}
