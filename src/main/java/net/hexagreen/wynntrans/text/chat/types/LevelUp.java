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

    public LevelUp(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.levelUp";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        Text t1 = Text.translatable(parentKey).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.GOLD));
        resultText.append(getCenterIndent(t1).append(t1).append("\n"));

        Matcher m2 = REGEX_LEVELUP.matcher(getSibling(2).getString());
        if(m2.find()) {
            Text t2 = Text.translatable(parentKey + ".nowOn", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            resultText.append(getCenterIndent(t2).append(t2).append("\n"));
        }

        resultText.append("\n");

        Matcher m3 = REGEX_NEXTAP.matcher(getSibling(4).getString());
        if(m3.find()) {
            Text num = Text.literal(m3.group(1)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            Text t3 = Text.translatable(parentKey + ".nextAP", num);
            resultText.append(getCenterIndent(t3).append(t3).append("\n"));
        }

        resultText.append("\n");

        for(int i = 6; getSiblings().size() > i; i++) {
            if(getSibling(i).getString().contains("+1 Ability Point")) {
                resultText.append(Text.translatable(parentKey + ".abilityPoint").setStyle(Style.EMPTY.withColor(Formatting.AQUA))).append(Text.translatable(parentKey + ".abilityPoint.guide").setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA))).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+2 Skill Points")) {
                resultText.append(Text.translatable(parentKey + ".skillPoint").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(Text.translatable(parentKey + ".skillPoint.guide").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+5 Maximum HP")) {
                resultText.append(Text.translatable(parentKey + ".healthPoint").setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Quest")) {
                String keyNewQuest = parentKey + ".newQuest";
                String valQuestName = getSibling(i).getString().substring(16).replace("[", "").replace("]", "").replace("À", "").replace("֎", "");
                if(valQuestName.contains("Mini-Quest - ")) {
                    keyNewQuest = parentKey + ".newMiniQuest";
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

                resultText.append(Text.translatable(keyNewQuest).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append(questText).append("\n");
                continue;
            }
            String hashOtherReward = DigestUtils.sha1Hex(getSibling(i).getString()).substring(0, 4);
            MutableText otherReward = Text.literal("+ ").setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            String keyOtherReward = "wytr.level." + m2.group(1) + "." + hashOtherReward;
            String valOtherReward = getSibling(i).getString().substring(4);

            if(WTS.checkTranslationExist(keyOtherReward, valOtherReward)) {
                otherReward.append(Text.translatable(keyOtherReward).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            }
            else {
                otherReward.append(valOtherReward);
            }
            resultText.append(otherReward);
        }
    }
}
