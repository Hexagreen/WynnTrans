package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DailyReward extends WynnChatText {
    private final String emeralds;
    private final String items;

    protected DailyReward(Text text, Pattern regex) {
        super(text, regex);
        this.emeralds = matcher.group(1);
        this.items = matcher.group(2);
    }

    public static DailyReward of(Text text, Pattern regex) {
        return new DailyReward(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "dailyReward";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)));
        if(emeralds != null) {
            resultText.append(newTranslate(parentKey + ".emerald", emeralds).setStyle(getStyle(1)));
            if(items != null) {
                resultText.append(newTranslate(parentKey + ".and").setStyle(getStyle(2)))
                        .append(newTranslate(parentKey + ".item", items).setStyle(getStyle(3)));
            }
        }
        else {
            resultText.append(newTranslate(parentKey + ".item", items).setStyle(getStyle(1)));
        }
        resultText.append(getSibling(-1));
    }
}
