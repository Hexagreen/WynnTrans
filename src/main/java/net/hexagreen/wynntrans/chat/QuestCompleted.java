package net.hexagreen.wynntrans.chat;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestCompleted extends WynnChatText {
    private static final Pattern REGEX_EXP = Pattern.compile("^\\+(\\d+) Experience Points$");
    private static final Pattern REGEX_EME = Pattern.compile("^\\+(\\d+) Emeralds$");
    private static final String func = rootKey + dirFunctional;
    private final String keyQuestName;
    private final String center;

    protected QuestCompleted(Text text, Pattern regex) {
        super(text, regex);
        this.keyQuestName = parentKey + getSibling(2).getSiblings().get(1).getString().replace(" ","");
        this.center = getCenterIndent();
    }

    public static QuestCompleted of(Text text, Pattern regex) {
        return new QuestCompleted(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "quest.";
    }

    @Override
    protected void build() {
        resultText = Text.empty()
                .append(newTranslate(func + "questCompleted").setStyle(getSibling(1).getSiblings().get(0).getStyle())).append("\n")
                .append(center)
                .append(newTranslate(keyQuestName).setStyle(getSibling(2).getSiblings().get(1).getStyle()))
                .append("\n\n")
                .append(newTranslate(func + "reward").setStyle(getSibling(4).getSiblings().get(0).getStyle())).append("\n");

        int rewardIndex = 1;
        for(int i = 5; inputText.getSiblings().size() > i; i++) {
            Matcher m1 = REGEX_EXP.matcher(getSibling(i).getSiblings().get(1).getString());
            if(m1.find()) {
                resultText.append(getSibling(i).getSiblings().get(0))
                        .append(newTranslate(func + "reward.experience", m1.group(1)).setStyle(getSibling(i).getSiblings().get(1).getStyle()))
                        .append("\n");
                continue;
            }
            Matcher m2 = REGEX_EME.matcher(getSibling(i).getSiblings().get(1).getString());
            if(m2.find()) {
                resultText.append(getSibling(i).getSiblings().get(0))
                        .append(newTranslate(func + "reward.emerald", m2.group(1)).setStyle(getSibling(i).getSiblings().get(1).getStyle()))
                        .append("\n");
                continue;
            }
            String keyExclusiveReward = keyQuestName + ".reward_" + rewardIndex++;
            String valExclusiveReward = getSibling(i).getSiblings().get(1).getString();
            if(WTS.checkTranslationExist(keyExclusiveReward, valExclusiveReward)) {
                resultText.append(getSibling(i).getSiblings().get(0));
                resultText.append(newTranslate(keyExclusiveReward).setStyle(getSibling(i).getSiblings().get(1).getStyle()));
            }
            else {
                resultText.append(getSibling(i));
            }
            resultText.append("\n");
        }
    }
    
    private String getCenterIndent() {
        String criteria =
                MutableText.of(new TranslatableTextContent("wytr.func.questCompleted", null, TranslatableTextContent.EMPTY_ARGUMENTS))
                        .getString();
        String noIndentCrit = criteria.replaceAll("^ +", "");
        double byteperchar = (double) noIndentCrit.getBytes().length / noIndentCrit.length();
        double spaceperchar;
        if(byteperchar > 1) spaceperchar = byteperchar / 1.5;
        else spaceperchar = 1;

        int center = criteria.length() - noIndentCrit.length() + (int)(noIndentCrit.length() / 2.0 * spaceperchar);
        String str =
                MutableText.of(new TranslatableTextContent(keyQuestName, null, TranslatableTextContent.EMPTY_ARGUMENTS))
                        .getString();
        int newIndent = center - (int)(0.5 + str.getBytes().length / 2.0 / spaceperchar);
        String space = "";
        for(int i = newIndent; 0 <= i; i--) space = space.concat(" ");

        return space;
    }
}
