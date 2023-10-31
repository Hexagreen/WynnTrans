package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestCompleted extends WynnChatText implements ICenterAligned {
    private static final Pattern REGEX_EXP = Pattern.compile("^\\+(\\d+) Experience Points$");
    private static final Pattern REGEX_EME = Pattern.compile("^\\+(\\d+) Emeralds$");
    private static final String func = rootKey + dirFunctional;
    private final String keyQuestName;
    private final String valQuestName;

    protected QuestCompleted(Text text, Pattern regex) {
        super(text, regex);
        this.valQuestName = getSibling(2).getSiblings().get(1).getString();
        this.keyQuestName = parentKey + valQuestName.replace(" ","").replace("'", "");
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
        resultText = Text.empty().append("\n")
                .append(getCenterIndent(func + "questCompleted"))
                .append(newTranslate(func + "questCompleted").setStyle(getSibling(1).getSiblings().get(0).getStyle())).append("\n");

        if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
            resultText.append(getCenterIndent(keyQuestName))
                    .append(newTranslate(keyQuestName).setStyle(getSibling(2).getSiblings().get(1).getStyle()));
        }
        else {
            Text origin = getSibling(2).getSiblings().get(1);
            resultText.append(getCenterIndent(origin)).append(origin);
        }

        resultText.append("\n\n")
                .append(newTranslate(func + "reward").setStyle(getSibling(4).getSiblings().get(0).getStyle())).append("\n");

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
            String valExclusiveReward = getSibling(i).getSiblings().get(1).getString();
            String hash = DigestUtils.sha1Hex(valExclusiveReward).substring(0, 4);
            String keyExclusiveReward = keyQuestName + ".reward_" + hash;
            if(WTS.checkTranslationExist(keyExclusiveReward, valExclusiveReward)) {
                resultText.append(getSibling(i).getSiblings().get(0));
                resultText.append(newTranslate(keyExclusiveReward).setStyle(getSibling(i).getSiblings().get(1).getStyle()));
            }
            else {
                resultText.append(getSibling(i));
            }
        }
        resultText.append("\n");
    }
}
