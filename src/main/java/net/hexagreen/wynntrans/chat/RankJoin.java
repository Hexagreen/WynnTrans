package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RankJoin extends WynnChatText {
    protected RankJoin(Text text, Pattern regex) {
        super(text, regex);
    }

    public static RankJoin of(Text text, Pattern regex) {
        return new RankJoin(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "rankJoin";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(0))
                .append(getSibling(1))
                .append(newTranslate(parentKey).setStyle(getStyle(2)));
    }
}
