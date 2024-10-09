package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class LittleInform extends WynnChatText {
    private final Text header;
    private final String keyInform;

    public LittleInform(Text text, Pattern regex) {
        super(text, regex);
        this.header = getHeader();
        this.keyInform = DigestUtils.sha1Hex(inputText.getString()).substring(0, 12);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "inform.";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(header);

        if(getSiblings().isEmpty()) {
            String valInform = getContentString().replaceAll("^§.\\[§.!§.] ", "");
            if(WTS.checkTranslationExist(parentKey + keyInform, valInform)) {
                resultText.append(Text.translatable(parentKey + keyInform));
            }
            else {
                resultText.append(valInform);
            }
        }
        else {
            resultText = inputText.copyContentOnly().append(getSibling(0)).append(getSibling(1));
            for(int i = 2; i < getSiblings().size(); i++) {
                String valInform = getSibling(i).getString();

                if(WTS.checkTranslationExist(parentKey + keyInform + "_" + (i - 1), valInform)) {
                    resultText.append(Text.translatable(parentKey + keyInform + "_" + (i - 1)).setStyle(getStyle(i)));
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
