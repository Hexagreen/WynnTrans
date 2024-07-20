package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
        resultText.append(newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        if(emeralds != null) {
            resultText.append(newTranslate(parentKey + ".emerald", emeralds).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
            if(items != null) {
                resultText.append(newTranslate(parentKey + ".and").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(newTranslate(parentKey + ".item", items).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
            }
        }
        else {
            resultText.append(newTranslate(parentKey + ".item", items).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        }
        resultText.append("]").setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    }
}
