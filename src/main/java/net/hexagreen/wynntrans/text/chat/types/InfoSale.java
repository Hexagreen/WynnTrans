package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class InfoSale extends WynnChatText {
    private final String hash;
    private final Text original;
    private final Text timer;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\[Sale] ").matcher(text.getString()).find();
    }

    private static Text cutoffTail(Text text) {
        MutableText result = Text.empty();
        for(int i = 0; text.getSiblings().size() - 2 > i; i++) {
            result.append(text.getSiblings().get(i));
        }
        return result;
    }

    public InfoSale(Text text) {
        super(cutoffTail(text));
        this.original = text;
        this.hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
        this.timer = initTimer(text.getSiblings().getLast());
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "saleInfo";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey).setStyle(getStyle(0)));

        for(int index = 1; getSiblings().size() > index; index++) {
            String keySibling = translationKey + "." + hash + "." + (index - 1);
            String valSibling = getContentString(index);
            if(valSibling.isEmpty()) resultText.append("");
            if(valSibling.contains("wynncraft.com")) {
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

        resultText.append(Text.translatable(translationKey + ".end", timer).setStyle(original.getSiblings().get(original.getSiblings().size() - 2).getStyle()));
    }

    private Text initTimer(Text text) {
        String str = text.getString().replace(".", "");
        return ITime.translateTime(str).setStyle(text.getStyle());
    }
}
