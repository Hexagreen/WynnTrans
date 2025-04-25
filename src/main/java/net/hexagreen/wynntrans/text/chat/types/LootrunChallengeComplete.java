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

public class LootrunChallengeComplete extends WynnChatText implements ILootrun {

    public LootrunChallengeComplete(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.lootrun.challengeComplete";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text title = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true));
        Text desc = Text.translatable(translationKey + ".desc").setStyle(GRAY);

        List<List<Text>> textChunks = getTextChunks(getSiblings());
        textChunks.removeFirst();
        resultText = Text.empty()
                .append(centerAlign(title)).append("\n")
                .append(centerAlign(desc)).append("\n");
        if(textChunks.isEmpty()) return;
        else resultText.append("\n");

        if(textChunks.getFirst().getFirst().getString().matches("§7.§7\\[\\+\\d+%.+")) {
            List<Text> effectsBody = textChunks.removeFirst();
            Style effectStyle = GRAY;
            Text damageInc = translateEffect(effectsBody.getFirst().getSiblings().getLast().getString()).setStyle(effectStyle);
            Text healthInc = translateEffect(effectsBody.getLast().getSiblings().getLast().getString()).setStyle(effectStyle);
            resultText.append(centerAlign(damageInc)).append("\n")
                    .append(centerAlign(healthInc).append("\n"));
            if(textChunks.isEmpty()) return;
            else resultText.append("\n");
        }

        List<Text> challengeReward = placeTextToBiSection(textChunks.stream()
                .map(this::mergeTextFromBiSection)
                .flatMap(Arrays::stream)
                .map(this::translateReward)
                .filter(Objects::nonNull)
                .toList());

        challengeReward.forEach(t -> resultText.append(t).append("\n"));
    }

    private List<Text> translateReward(Text text) {
        if(text.getString().isEmpty()) return null;
        List<Text> siblings = text.getSiblings();

        String rewardTypeString = siblings.getFirst().getString();
        Style rewardTypeStyle = parseStyleCode(rewardTypeString);

        List<Text> result = new ArrayList<>();
        if(rewardTypeString.contains("§6§lBoon")) {
            result.add(Text.translatable("wytr.lootrun.boon").setStyle(rewardTypeStyle));
            String boon = normalizeStringForKey(siblings.getLast().getString().replaceAll("§.", ""));
            result.add(Text.translatable("wytr.lootrun.boon." + boon).setStyle(GRAY));
        }
        else if(rewardTypeString.contains("§5§lCurses")) {
            result.add(Text.translatable("wytr.lootrun.curse").setStyle(rewardTypeStyle));
            Matcher effectMatcher = Pattern.compile("\\[.+?]").matcher(siblings.getLast().getString());
            while(effectMatcher.find()) {
                result.add(translateEffect(effectMatcher.group()).setStyle(GRAY));
            }
        }
        else if(rewardTypeString.contains("§b§lMission Started")) {
            result.add(Text.translatable("wytr.lootrun.mission").setStyle(rewardTypeStyle));
            String mission = normalizeStringForKey(siblings.getLast().getString().replaceAll("§.", ""));
            result.add(Text.translatable("wytr.lootrun.mission." + mission).setStyle(GRAY));
        }
        else {
            String mission = normalizeStringForKey(rewardTypeString.replaceAll("§.", ""));
            result.add(Text.translatable("wytr.lootrun.mission." + mission).setStyle(rewardTypeStyle));
            Matcher effectMatcher = Pattern.compile("\\[.+?]").matcher(siblings.getLast().getString());
            while(effectMatcher.find()) {
                result.add(translateEffect(effectMatcher.group()).setStyle(GRAY));
            }
        }

        return result;
    }
}