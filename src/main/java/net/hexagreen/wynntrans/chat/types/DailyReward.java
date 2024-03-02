package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DailyReward extends WynnChatText {
    private final String emeralds;
    private final String items;

    public DailyReward(Text text, Pattern regex) {
        super(text, regex);
        this.emeralds = matcher.group(1);
        this.items = matcher.group(2);
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
