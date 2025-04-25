package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ILootrun;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LootrunSummary extends WynnChatText implements ILootrun {

    public LootrunSummary(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.lootrun.endSummary";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text title = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.GOLD));
        Text desc = Text.translatable(translationKey + ".desc").setStyle(GRAY);

        List<List<Text>> textChunks = getTextChunks(getSiblings());
        textChunks.removeFirst();

        List<Text> summaries = placeTextToBiSection(textChunks.stream()
                .map(this::mergeTextFromBiSection)
                .flatMap(Arrays::stream)
                .map(this::translateSummary)
                .filter(Objects::nonNull)
                .toList());

        resultText = Text.empty()
                .append(centerAlign(title)).append("\n")
                .append(centerAlign(desc)).append("\n");
        summaries.forEach(t -> resultText.append("\n").append(t));
    }

    private List<Text> translateSummary(Text text) {
        if(text.getSiblings().getFirst().getString().contains("§aRewards")) return translateReward(text);
        else return translateStatistic(text);
    }

    private List<Text> translateReward(Text text) {
        List<Text> result = new ArrayList<>();
        result.add(Text.translatable(translationKey + ".reward").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));

        Matcher matcher = Pattern.compile("(§.\\d+§7) .+?").matcher(text.getString());
        for(int i = 0; matcher.find(); i++) {
            String number = matcher.group(1);
            String key = null;
            switch(i) {
                case 0 -> key = translationKey + ".reward.pull";
                case 1 -> key = translationKey + ".reward.reroll";
                case 2 -> key = translationKey + ".reward.sacrifice";
                case 3 -> key = translationKey + ".reward.experience";
            }
            if(key != null) result.add(Text.translatable(key, number).setStyle(GRAY));
        }

        return result;
    }

    private List<Text> translateStatistic(Text text) {
        List<Text> result = new ArrayList<>();
        result.add(Text.translatable(translationKey + ".stat").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));

        Matcher matcher = Pattern.compile(": (§.[\\d:]+)").matcher(text.getString());
        for(int i = 0; matcher.find(); i++) {
            String number = matcher.group(1);
            String key = null;
            switch(i) {
                case 0 -> key = translationKey + ".stat.time";
                case 1 -> key = translationKey + ".stat.kill";
                case 2 -> key = translationKey + ".stat.chest";
                case 3 -> key = translationKey + ".stat.challenge";
            }
            if(key != null) result.add(Text.translatable(key, number).setStyle(GRAY));
        }

        return result;
    }
}
