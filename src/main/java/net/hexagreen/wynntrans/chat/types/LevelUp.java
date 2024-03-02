package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.ICenterAligned;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelUp extends WynnChatText implements ICenterAligned {
    private static final Pattern REGEX_LEVELUP = Pattern.compile("^ +You are now combat level (\\d+)");
    private static final Pattern REGEX_NEXTAP = Pattern.compile("^ +Only (\\d+) more levels? until your next Ability Point");

    public LevelUp(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "levelUp";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        Text t1 = newTranslate(parentKey).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.GOLD));
        resultText.append(Text.literal(getCenterIndent(t1)).append(t1).append("\n"));

        Matcher m2 = REGEX_LEVELUP.matcher(getSibling(2).getString());
        if(m2.find()) {
            Text t2 = newTranslate(parentKey + ".nowOn", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            resultText.append(Text.literal(getCenterIndent(t2)).append(t2).append("\n"));
        }

        resultText.append("\n");

        Matcher m3 = REGEX_NEXTAP.matcher(getSibling(4).getString());
        if(m3.find()) {
            Text num = Text.literal(m3.group(1)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
            Text t3 = newTranslate(parentKey + ".nextAP", num);
            resultText.append(Text.literal(getCenterIndent(t3)).append(t3).append("\n"));
        }

        resultText.append("\n");

        for(int i = 6; inputText.getSiblings().size() > i; i++) {
            if(getSibling(i).getString().contains("+1 Ability Point")) {
                resultText.append(newTranslate(parentKey + ".abilityPoint").setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
                        .append(newTranslate(parentKey + ".abilityPoint.guide").setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+2 Skill Points")) {
                resultText.append(newTranslate(parentKey + ".skillPoint").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(newTranslate(parentKey + ".skillPoint.guide").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+5 Maximum HP")) {
                resultText.append(newTranslate(parentKey + ".healthPoint").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+1 Maximum Soul Points")) {
                String amount = getSibling(i).getSiblings().get(1).getString().replaceAll("\\D", "");
                resultText.append(newTranslate(parentKey + ".soulPoint").setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(newTranslate(parentKey + ".soulPoint.amount", amount).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)))
                        .append("\n");
                continue;
            }
            if(getSibling(i).getString().contains("+ New Quest")) {
                String keyNewQuest = parentKey + ".newQuest";
                String valQuestName = getSibling(i).getSiblings().get(1).getString()
                        .replace("[", "").replace("]", "")
                        .replace("À", "").replace("֎", "");
                if(valQuestName.contains("Mini-Quest - ")) {
                    keyNewQuest = parentKey + ".newMiniQuest";
                    valQuestName = valQuestName.replace("Mini-Quest - ", "");
                }

                String keyQuestName = "wytr.quest." + normalizeStringQuestName(valQuestName);

                Text questText;
                if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
                    questText = Text.literal("[" + newTranslate(keyQuestName).getString() + "]").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
                }
                else {
                    questText = getSibling(i).getSiblings().get(1);
                }

                resultText.append(newTranslate(keyNewQuest).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append(questText)
                        .append("\n");
                continue;
            }
            String hashOtherReward = DigestUtils.sha1Hex(getSibling(i).getString()).substring(0, 4);
            MutableText otherReward = Text.literal("+ ").setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            if(getSibling(i).getSiblings().size() == 2) {
                Text origin = getSibling(i).getSiblings().get(1);
                String keyOtherReward = "wytr.level." + m2.group(1) + "." + hashOtherReward;
                String valOtherReward = origin.getString();

                if(WTS.checkTranslationExist(keyOtherReward, valOtherReward)) {
                    otherReward.append(newTranslate(keyOtherReward).setStyle(origin.getStyle()));
                }
                else {
                    otherReward.append(origin);
                }
            }
            else {
                for(int index = 1; index < getSibling(i).getSiblings().size(); index++) {
                    Text origin = getSibling(i).getSiblings().get(index);
                    String keyOtherReward = "wytr.level." + m2.group(1) + "." + hashOtherReward + "_" + index;
                    String valOtherReward = origin.getString();

                    if(WTS.checkTranslationExist(keyOtherReward, valOtherReward)) {
                        otherReward.append(newTranslate(keyOtherReward).setStyle(origin.getStyle()));
                    }
                    else {
                        otherReward.append(origin);
                    }
                }
            }
            resultText.append(otherReward);
        }
    }
}
