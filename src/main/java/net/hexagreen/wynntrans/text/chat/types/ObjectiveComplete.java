package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Objectives;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectiveComplete extends WynnChatText implements ISpaceProvider {
    private static final Pattern REGEX_EXP = Pattern.compile("§7\\+§f(\\d+)§7 Experience Points");
    private static final Pattern REGEX_EME = Pattern.compile("\\+(\\d+) Emeralds");
    private static final String func = rootKey + "func.";
    private final Style titleStyle;
    private final Style objectiveNameStyle;
    private final MutableText objective;
    private String keyEName = null;
    private String valEName = null;
    private Style styleEName = null;
    private Style styleEReward = null;

    public ObjectiveComplete(Text text) {
        super(text);
        this.titleStyle = parseStyleCode(getSibling(1).getString().replaceAll("\\[.+]", "").replaceAll(" ", ""));

        this.objectiveNameStyle = parseStyleCode(getSibling(2).getString().replaceAll("(?!§.) +.+", ""));
        this.objective = getObjective();

        if(text.getSiblings().size() == 8) {
            this.styleEName = parseStyleCode(getSibling(4).getString());
            this.valEName = getSibling(4).getString().replaceAll(" +(§.)+", "");
            String hash2 = DigestUtils.sha1Hex(valEName).substring(0, 4);
            this.keyEName = "wytr.eventInfo.eventName." + hash2;
            this.styleEReward = parseStyleCode(getSibling(5).getString());
        }
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "objective.";
    }

    @Override
    protected void build() {
        MutableText titleText = Text.translatable(func + "objCompleted").setStyle(titleStyle);
        resultText = Text.empty().append("\n")
                .append(centerAlign(titleText))
                .append("\n");

        resultText.append(centerAlign(objective.setStyle(objectiveNameStyle)));

        resultText.append("\n\n");

        if(titleStyle.equals(Style.EMPTY.withColor(Formatting.DARK_GREEN))) {
            resultText.append("     ").append(Text.translatable(func + "info").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE))).append("\n");

            for(int i = 4; getSiblings().size() > i; i++) {
                Matcher m1 = REGEX_EXP.matcher(getSibling(i).getString());
                if(m1.find()) {
                    resultText.append("     - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)).append(Text.translatable(func + "reward.experience", m1.group(1)).setStyle(GRAY)).append("\n");
                    continue;
                }
                Matcher m2 = REGEX_EME.matcher(getSibling(i).getString());
                if(m2.find()) {
                    resultText.append("     - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)).append(Text.translatable(func + "reward.emerald", m2.group(1)).setStyle(GRAY)).append("\n");
                    continue;
                }
                resultText.append(getSibling(i));
                resultText.append("\n");
            }

            return;
        }

        if(keyEName != null) {
            if(WTS.checkTranslationExist(keyEName, valEName)) {
                Text text = Text.translatable(keyEName).setStyle(styleEName);
                resultText.append(centerAlign(text));
            }
            else {
                Text origin = Text.literal(valEName).setStyle(styleEName);
                resultText.append(centerAlign(origin));
            }
            Text origin = Text.literal(getSibling(5).getString().replaceAll(" +§.", "")).setStyle(styleEReward);
            resultText.append("\n").append(centerAlign(origin)).append("\n\n");
        }

        MutableText rewardText = Text.translatable(func + "objReward").setStyle(getSibling(-1).getSiblings().get(1).getStyle());
        resultText.append(centerAlign(rewardText))
                .append("\n");
    }

    private MutableText getObjective() {
        Objectives normalizedObjective = Objectives.findNormalized(getSibling(2).getString().replaceAll("^§. +", ""));
        return normalizedObjective.getTranslatedText();
    }
}
