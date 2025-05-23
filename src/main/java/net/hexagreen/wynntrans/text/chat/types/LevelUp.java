package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelUp extends WynnChatText implements ISpaceProvider {
    private static final Pattern REGEX_LEVELUP = Pattern.compile("You are now combat level (\\d+)");
    private static final Pattern REGEX_NEXTAP = Pattern.compile("^ +§6Only §e(\\d+) more levels?§6 until your next§e Ability Point");

    public LevelUp(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.levelUp";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        Text t1 = Text.translatable(translationKey).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.GOLD));
        resultText.append(centerAlign(t1).append("\n"));

        Matcher m2 = REGEX_LEVELUP.matcher(getSibling(2).getString());
        if(m2.find()) {
            Text t2 = Text.translatable(translationKey + ".nowOn", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            resultText.append(centerAlign(t2).append("\n"));
        }

        resultText.append("\n");

        Matcher m3 = REGEX_NEXTAP.matcher(getSibling(4).getString());
        if(m3.find()) {
            Text num = Text.literal(m3.group(1)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            Text t3 = Text.translatable(translationKey + ".nextAP", num);
            resultText.append(centerAlign(t3).append("\n"));
        }

        resultText.append("\n");

        for(int i = 6; getSiblings().size() > i; i++) {
            if(getSibling(i).getString().contains("+1 Ability Point")) {
                resultText.append(Text.translatable(translationKey + ".abilityPoint").setStyle(Style.EMPTY.withColor(Formatting.AQUA))).append(Text.translatable(translationKey + ".abilityPoint.guide").setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA))).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+2 Skill Points")) {
                resultText.append(Text.translatable(translationKey + ".skillPoint").setStyle(GRAY)).append(Text.translatable(translationKey + ".skillPoint.guide").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+5 Maximum HP")) {
                resultText.append(Text.translatable(translationKey + ".healthPoint").setStyle(GRAY)).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Quest")) {
                String keyNewQuest = translationKey + ".newQuest";
                String valQuestName = getSibling(i).getString().substring(16).replace("[", "").replace("]", "").replace("À", "").replace("֎", "");
                if(valQuestName.contains("Mini-Quest - ")) {
                    keyNewQuest = translationKey + ".newMiniQuest";
                    valQuestName = valQuestName.replace("Mini-Quest - ", "");
                }

                String keyQuestName = "wytr.quest." + normalizeStringForKey(valQuestName);

                Text questText;
                if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
                    questText = Text.literal("[" + Text.translatable(keyQuestName).getString() + "]").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
                }
                else {
                    questText = Text.literal(getSibling(i).getString().substring(16)).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
                }

                resultText.append(Text.translatable(keyNewQuest).setStyle(GRAY)).append(questText).append("\n");
                continue;
            }
            String hashOtherReward = DigestUtils.sha1Hex(getSibling(i).getString()).substring(0, 4);
            MutableText otherReward = Text.literal("+ ").setStyle(GRAY);
            String keyOtherReward = "wytr.level." + m2.group(1) + "." + hashOtherReward;
            String valOtherReward = getSibling(i).getString().substring(4);

            if(WTS.checkTranslationExist(keyOtherReward, valOtherReward)) {
                otherReward.append(Text.translatable(keyOtherReward).setStyle(GRAY));
            }
            else {
                otherReward.append(valOtherReward);
            }
            resultText.append(otherReward);
        }
    }
}
