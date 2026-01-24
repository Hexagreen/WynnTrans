package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.List;
import java.util.regex.Pattern;

public class InfoEvent extends WynnChatText {
    private final String keyEName;
    private final String valEName;
    private final String hash;
    private final Text original;
    private final Text timer;
    private final boolean isLegacy;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\[Event] ").matcher(text.getString()).find();
    }

    private static Text cutoffTail(Text text) {
        MutableText result = Text.empty();
        List<Text> siblings = text.getSiblings();
        for(Text sibling : siblings) {
            String string = sibling.getString();
            if(string.contains("[Event]")) continue;
            if(string.contains("The festival closes its doors in")) {
                String cut = string.replaceFirst("The festival closes its doors in ", "");
                if(!cut.isBlank()) result.append(Text.literal(cut).setStyle(sibling.getStyle()));
                break;
            }
            else result.append(sibling);
        }
        return result;
    }

    public InfoEvent(Text text) {
        super(cutoffTail(text));
        this.original = text;
        this.hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
        this.isLegacy = text.getString().contains("[Event]");
        this.valEName = text.getSiblings().get(isLegacy ? 1 : 0).getString().replace(": ", "");
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
        List<Text> originalSiblings = original.getSiblings();

        resultText = Text.empty();
        if(isLegacy)
            resultText.append(Text.translatable(translationKey).setStyle(originalSiblings.getFirst().getStyle()));

        int startIdx = isLegacy ? 1 : 0;

        if(WTS.checkTranslationExist(keyEName, valEName)) {
            resultText.append(Text.translatable(keyEName).setStyle(originalSiblings.get(startIdx).getStyle()).append(": "));
        }
        else {
            resultText.append(originalSiblings.get(1));
        }

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

        resultText.append(Text.translatable(translationKey + ".end", timer).setStyle(originalSiblings.get(originalSiblings.size() - 2).getStyle()));
    }

    private Text initTimer(Text text) {
        String str = text.getString().replace(".", "");
        return ITime.translateTime(str).setStyle(text.getStyle());
    }
}
