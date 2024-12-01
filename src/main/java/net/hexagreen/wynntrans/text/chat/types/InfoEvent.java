package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class InfoEvent extends WynnChatText {
    private final String keyEName;
    private final String valEName;
    private final String hash;
    private final Text original;
    private final Text timer;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\[Event] ").matcher(text.getString()).find();
    }

    private static Text cutoffTail(Text text) {
        MutableText result = Text.empty();
        int size = text.getSiblings().size();
        result.append(text.getSiblings().getFirst());
        for(int i = 2; size - 2 > i; i++) {
            result.append(text.getSiblings().get(i));
        }
        return result;
    }

    public InfoEvent(Text text) {
        super(cutoffTail(text));
        this.original = text;
        this.hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
        this.valEName = text.getSiblings().get(1).getString().replace(": ", "");
        String hash2 = DigestUtils.sha1Hex(valEName).substring(0, 4);
        this.keyEName = translationKey + ".eventName." + hash2;
        this.timer = initTimer(text.getSiblings().getLast());
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "eventInfo";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey).setStyle(getStyle(0)));

        if(WTS.checkTranslationExist(keyEName, valEName)) {
            resultText.append(Text.translatable(keyEName).setStyle(original.getSiblings().get(1).getStyle()).append(": "));
        }
        else {
            resultText.append(original.getSiblings().get(1));
        }

        for(int index = 1; getSiblings().size() > index; index++) {
            String keySibling = translationKey + "." + hash + "_" + (index - 1);
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

        resultText.append(Text.translatable(translationKey + ".end", timer).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }

    private Text initTimer(Text text) {
        String str = text.getString().replace(".", "");
        return ITime.translateTime(str).setStyle(text.getStyle());
    }
}
