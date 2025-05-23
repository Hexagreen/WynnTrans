package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaveCompletedLiteral extends WynnChatText implements ISpaceProvider {
    private static final Pattern REGEX_EXP = Pattern.compile("§7\\+(\\d+) Experience Points");
    private static final Pattern REGEX_EME = Pattern.compile("§7\\+(\\d+) §aEmeralds");
    private static final Style LIGHT_PURPLE = Style.EMPTY.withColor(Formatting.LIGHT_PURPLE);
    private static final String func = rootKey + "func.";
    private final String keyCaveName;
    private final String valCaveName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^ \\n§2 +\\[Cave Completed]").matcher(text.getString()).find();
    }

    private static Text splitTextBody(Text text) {
        String[] lines = text.getString().split("\\n");
        MutableText result = Text.empty();
        for(String line : lines) {
            result.append(line);
        }

        return result.copy();
    }

    public CaveCompletedLiteral(Text text) {
        super(splitTextBody(text));
        this.valCaveName = getSibling(2).getString().replaceAll("§. +§.", "");
        this.keyCaveName = translationKey + normalizeStringForKey(valCaveName);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "cave.";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n");

        Text title;
        title = Text.translatable(func + "caveCompleted").setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN));

        resultText.append(centerAlign(title)).append("\n");


        Text t0;
        if(WTS.checkTranslationExist(keyCaveName, valCaveName)) {
            Style t0Style = parseStyleCode(getSibling(2).getString().replace(valCaveName, "").replaceAll(" ", ""));
            t0 = Text.translatable(keyCaveName).setStyle(t0Style);
        }
        else {
            t0 = Text.literal(getSibling(2).getString().replaceAll(" +(?=§)", ""));
        }

        resultText.append(centerAlign(t0)).append("\n\n").append("           ").append(Text.translatable(func + "reward")
                .setStyle(LIGHT_PURPLE)).append("\n");


        for(int i = 5; getSiblings().size() > i; i++) {
            Matcher m1 = REGEX_EXP.matcher(getSibling(i).getString());
            if(m1.find()) {
                resultText.append("           - ").setStyle(LIGHT_PURPLE)
                        .append(Text.translatable(func + "reward.experience", m1.group(1)).setStyle(GRAY)).append("\n");
                continue;
            }
            Matcher m2 = REGEX_EME.matcher(getSibling(i).getString());
            if(m2.find()) {
                resultText.append("           - ").setStyle(LIGHT_PURPLE)
                        .append(Text.translatable(func + "reward.emerald", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GREEN))).append("\n");
                continue;
            }
            String valExclusiveReward = getSibling(i).getString().replaceAll("§d +- §7", "");
            String num;
            if(valExclusiveReward.matches("^\\+\\d+ .+")) {
                String[] split = valExclusiveReward.split(" ", 2);
                num = split[0] + " ";
                valExclusiveReward = split[1];
            }
            else {
                num = "+";
                valExclusiveReward = valExclusiveReward.replaceFirst("^\\+", "");
            }
            Text _val = Text.literal(valExclusiveReward).setStyle(GRAY);
            Text itemName = new ItemName(_val).returnNullMode().textAsMutable();
            if(itemName != null) {
                resultText.append("           - ").setStyle(LIGHT_PURPLE);
                resultText.append(Text.literal(num).setStyle(GRAY));
                resultText.append(itemName).append("\n");
                continue;
            }

            String hash = DigestUtils.sha1Hex(valExclusiveReward).substring(0, 4);
            String keyExclusiveReward = keyCaveName + ".reward." + hash;
            if(WTS.checkTranslationExist(keyExclusiveReward, valExclusiveReward)) {
                resultText.append("           - ").setStyle(LIGHT_PURPLE);
                resultText.append(Text.literal(num).setStyle(GRAY));
                resultText.append(Text.translatable(keyExclusiveReward).setStyle(GRAY));
            }
            else {
                resultText.append(getSibling(i));
            }
            resultText.append("\n");
        }
    }
}
