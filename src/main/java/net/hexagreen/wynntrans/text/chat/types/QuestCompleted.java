package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.chat.ICenterAligned;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestCompleted extends WynnChatText implements ICenterAligned {
    private static final Pattern REGEX_EXP = Pattern.compile("\\+(\\d+) (?:Combat )?Experience Points$");
    private static final Pattern REGEX_EME = Pattern.compile("\\+(.+) Emeralds$");
    private static final Pattern REGEX_PROF = Pattern.compile("\\+(\\d+) (.+) Experience");
    private static final String func = rootKey + "func.";
    private final String keyQuestName;
    private final String valQuestName;
    private String keyTitle = func + "questCompleted";

    public QuestCompleted(Text text, Pattern regex) {
        super(text, regex);
        this.valQuestName = getSibling(2).getString()
                .replaceAll("^§. +§.", "");
        this.keyQuestName = parentKey + normalizeStringForKey(valQuestName);
        if(getSibling(1).getString().contains("Mini-Quest")) {
            this.keyTitle = func + "miniQuestCompleted";
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + "quest.";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n")
                .append(getCenterIndent(keyTitle))
                .append(newTranslate(keyTitle).setStyle(
                        parseStyleCode(
                                getSibling(1).getString()
                        )))
                .append("\n");

        if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
            Text questName = newTranslate(keyQuestName).setStyle(
                    parseStyleCode(
                            getSibling(2).getString().replaceAll("(§.) +(§.).+", "$1$2")
                    ));
            resultText.append(getCenterIndent(questName))
                    .append(questName);
        }
        else {
            Text origin = Text.literal(valQuestName).setStyle(Style.EMPTY.withColor(Formatting.YELLOW).withBold(true));
            resultText.append(getCenterIndent(origin)).append(origin);
        }

        resultText.append("\n\n")
                .append(newTranslate(func + "reward").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE))).append("\n");

        for(int i = 5; inputText.getSiblings().size() > i; i++) {
            Matcher m1 = REGEX_EXP.matcher(getSibling(i).getString());
            if(m1.find()) {
                resultText.append(Text.literal("            - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)))
                        .append(newTranslate(func + "reward.experience", m1.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append("\n");
                continue;
            }

            Matcher m2 = REGEX_EME.matcher(getSibling(i).getString());
            if(m2.find()) {
                resultText.append(Text.literal("            - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)))
                        .append(newTranslate(func + "reward.emerald", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append("\n");
                continue;
            }

            Matcher m3 = REGEX_PROF.matcher(getSibling(i).getString());
            if(m3.find()) {
                Text profText = Profession.getProfession(m3.group(2)).getTextWithIcon();
                resultText.append(Text.literal("            - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)))
                        .append(newTranslate(func + "reward.pExperience", m3.group(1), profText).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append("\n");
                continue;
            }

            String valExclusiveReward = getSibling(i).getString().replaceAll(".+\\+", "+");
            String hash = DigestUtils.sha1Hex(valExclusiveReward).substring(0, 4);
            String keyExclusiveReward = keyQuestName + ".reward_" + hash;
            if(WTS.checkTranslationExist(keyExclusiveReward, valExclusiveReward)) {
                resultText.append(Text.literal("            - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                resultText.append(newTranslate(keyExclusiveReward).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                        .append("\n");
            }
            else {
                resultText.append(getSibling(i)).append("\n");
            }
        }
    }
}
