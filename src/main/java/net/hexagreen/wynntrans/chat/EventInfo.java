package net.hexagreen.wynntrans.chat;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class EventInfo extends WynnChatText {
    private final String keyEName;
    private final String valEName;
    private final String hash;
    private final Text original;
    private final Text timer;

    public EventInfo(Text text, Pattern regex) {
        super(cutoffTail(text), regex);
        this.original = text;
        this.hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
        this.valEName = text.getSiblings().get(1).getString().replace(": ", "");
        String hash2 = DigestUtils.sha1Hex(valEName).substring(0, 4);
        this.keyEName = parentKey + ".eventName." + hash2;
        this.timer = getTimer(text.getSiblings().get(text.getSiblings().size() - 1));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "eventInfo";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)));

        if(WTS.checkTranslationExist(keyEName, valEName)) {
            resultText.append(newTranslate(keyEName).setStyle(original.getSiblings().get(1).getStyle()).append(": "));
        }
        else {
            resultText.append(original.getSiblings().get(1));
        }

        for (int index = 1; inputText.getSiblings().size() > index; index++) {
            String keySibling = parentKey + "." + hash + "_" + (index - 1);
            String valSibling = getContentLiteral(index);
            if(valSibling.isEmpty()) resultText.append("");
            if(valSibling.contains("wynncraft.com")) {
                resultText.append(getSibling(index));
                continue;
            }
            if (WTS.checkTranslationExist(keySibling, valSibling)) {
                resultText.append(newTranslate(keySibling).setStyle(getStyle(index)));
            }
            else {
                resultText.append(getSibling(index));
            }
        }

        resultText.append(newTranslate(parentKey + ".end", timer).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }

    private static Text cutoffTail(Text text) {
        MutableText result = Text.empty();
        int size = text.getSiblings().size();
        result.append(text.getSiblings().get(0));
        for(int i = 2; size - 2 > i; i++) {
            result.append(text.getSiblings().get(i));
        }
        return result;
    }

    private static Text getTimer(Text text) {
        String str = text.getString().replace(".", "");
        return Text.literal(str).setStyle(text.getStyle());
    }
}
