package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DailyReward extends WynnChatText {
    private final String emeralds;
    private final String items;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§7\\[Daily Rewards: (?:§a(\\d+) emeralds§7)?(?: and )?(?:§b(\\d+) items§7)?]$").matcher(text.getString()).find();
    }

    public DailyReward(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("^§7\\[Daily Rewards: (?:§a(\\d+) emeralds§7)?(?: and )?(?:§b(\\d+) items§7)?]$").matcher(inputText.getString());
        boolean ignore = matcher.find();
        this.emeralds = matcher.group(1);
        this.items = matcher.group(2);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.dailyReward";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey).setStyle(GRAY));
        if(emeralds != null) {
            resultText.append(Text.translatable(translationKey + ".emerald", emeralds).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
            if(items != null) {
                resultText.append(Text.translatable(translationKey + ".and").setStyle(GRAY)).append(Text.translatable(translationKey + ".item", items).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
            }
        }
        else {
            resultText.append(Text.translatable(translationKey + ".item", items).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        }
        resultText.append("]").setStyle(GRAY);
    }
}
