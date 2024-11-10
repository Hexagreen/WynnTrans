package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class DailyReward extends WynnChatText {
    private final String emeralds;
    private final String items;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§7\\[Daily Rewards: (?:§a(\\d+) emeralds§7)?(?: and )?(?:§b(\\d+) items§7)?]$").matcher(text.getString()).find();
    }

    public DailyReward(Text text) {
        super(text, Pattern.compile("^§7\\[Daily Rewards: (?:§a(\\d+) emeralds§7)?(?: and )?(?:§b(\\d+) items§7)?]$"));
        this.emeralds = matcher.group(1);
        this.items = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.dailyReward";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        if(emeralds != null) {
            resultText.append(Text.translatable(parentKey + ".emerald", emeralds).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
            if(items != null) {
                resultText.append(Text.translatable(parentKey + ".and").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(Text.translatable(parentKey + ".item", items).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
            }
        }
        else {
            resultText.append(Text.translatable(parentKey + ".item", items).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        }
        resultText.append("]").setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    }
}
