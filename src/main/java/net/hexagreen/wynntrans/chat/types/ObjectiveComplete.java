package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.ICenterAligned;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectiveComplete extends WynnChatText implements ICenterAligned {
    private static final Pattern REGEX_EXP = Pattern.compile("\\+(\\d+) Experience Points");
    private static final Pattern REGEX_EME = Pattern.compile("\\+(\\d+) Emeralds");
    private static final String func = rootKey + dirFunctional;
    private String keyObjectiveName;
    private String valObjectiveName;
    private Object argObjectiveName = new Object[0];
    private String keyEName = null;
    private String valEName = null;

    public ObjectiveComplete(Text text, Pattern regex) {
        super(text, regex);
        this.valObjectiveName = getSibling(2).getSiblings().get(0).getString().replaceAll("^ +", "");
        this.keyObjectiveName = parentKey + valObjectiveName.replace(" ", "");
        normalizeKeyVal();

        if(text.getSiblings().size() == 8) {
            this.valEName = getSibling(4).getSiblings().get(1).getString();
            String hash2 = DigestUtils.sha1Hex(valEName).substring(0, 4);
            this.keyEName = "wytr.eventInfo.eventName." + hash2;
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + "objective.";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n")
                .append(getCenterIndent(func + "objCompleted"))
                .append(newTranslate(func + "objCompleted").setStyle(getSibling(1).getSiblings().get(1).getStyle())).append("\n");

        if(WTS.checkTranslationExist(keyObjectiveName, valObjectiveName)) {
            resultText.append(getCenterIndent(keyObjectiveName, argObjectiveName))
                    .append(newTranslate(keyObjectiveName, argObjectiveName).setStyle(getSibling(2).getSiblings().get(0).getStyle()));
        }
        else {
            resultText.append(getCenterIndent(Text.literal(valObjectiveName)))
                    .append(Text.literal(valObjectiveName).setStyle(getSibling(2).getSiblings().get(0).getStyle()));
        }

        resultText.append("\n\n");

        if(getSibling(1).getSiblings().get(1).getStyle().equals(Style.EMPTY.withColor(Formatting.DARK_GREEN))) {
            resultText.append(newTranslate(func + "info").setStyle(getSibling(3).getSiblings().get(0).getStyle()))
                    .append("\n");

            for(int i = 4; inputText.getSiblings().size() > i; i++) {
                Matcher m1 = REGEX_EXP.matcher(getSibling(i).getString());
                if(m1.find()) {
                    resultText.append(getSibling(i).getSiblings().get(0))
                            .append(newTranslate(func + "reward.experience", m1.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                            .append("\n");
                    continue;
                }
                Matcher m2 = REGEX_EME.matcher(getSibling(i).getString());
                if(m2.find()) {
                    resultText.append(getSibling(i).getSiblings().get(0))
                            .append(newTranslate(func + "reward.emerald", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GRAY)))
                            .append("\n");
                    continue;
                }
                resultText.append(getSibling(i));
                resultText.append("\n");
            }

            return;
        }

        if(keyEName != null) {
            if(WTS.checkTranslationExist(keyEName, valEName)) {
                Text text = newTranslate(keyEName).setStyle(getSibling(4).getSiblings().get(1).getStyle());
                resultText.append(getCenterIndent(text)).append(text);
            }
            else {
                Text origin = getSibling(4).getSiblings().get(1);
                resultText.append(getCenterIndent(origin)).append(origin);
            }
            Text origin = getSibling(5).getSiblings().get(1);
            resultText.append("\n").append(getCenterIndent(origin)).append(origin).append("\n\n");
        }

        resultText.append(getCenterIndent(func + "objReward"))
                .append(newTranslate(func + "objReward").setStyle(getSibling(-1).getSiblings().get(1).getStyle())).append("\n");
    }

    private void normalizeKeyVal() {
        NormalizedObjective normalizedObjective = NormalizedObjective.findNormalized(valObjectiveName);
        if(normalizedObjective != NormalizedObjective.NO_TYPE){
            this.keyObjectiveName = parentKey + normalizedObjective.normalizedKey;
            this.valObjectiveName = normalizedObjective.normalizedVal;
            this.argObjectiveName = normalizedObjective.normalizedArg;
        }
    }

    private enum NormalizedObjective {
        SLAY_LEVELED_MOBS(Pattern.compile("Slay Lv\\. (\\d+)\\+ Mobs"), "SlayLeveledMobs", "Slay Lv. %s+ Mobs"),
        TIERED_LOOT_CHEST(Pattern.compile("Loot Chests T(\\d)\\+"), "LootChestsTiered", "Loot Chests T%s+"),
        NO_TYPE(null, null, null);

        private final Pattern regex;
        private final String normalizedKey;
        private final String normalizedVal;
        private Object normalizedArg;

        NormalizedObjective(Pattern regex, String normalizedKey, String normalizedVal) {
            this.regex = regex;
            this.normalizedKey = normalizedKey;
            this.normalizedVal = normalizedVal;
            this.normalizedArg = null;
        }

        boolean isMatch(String string) {
            if(this == NO_TYPE) return false;
            Matcher m = this.regex.matcher(string);
            boolean result = m.find();
            if(result) {
                normalizedArg = m.group(1);
                return true;
            }
            return false;
        }

        static NormalizedObjective findNormalized(String string) {
            return Arrays.stream(NormalizedObjective.values())
                    .filter(normalizedObjective -> normalizedObjective.isMatch(string))
                    .findFirst().orElse(NO_TYPE);
        }
    }
}
