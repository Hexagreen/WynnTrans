package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GatheringXPGain extends WynnDisplayText {
    private static final Pattern regex = Pattern.compile("^(§.x\\d )§.\\[\\+(§.\\d+)§. (.)§7 .+ XP]( .+)(?:\\n(§..+))?");
    private final String multiplier;
    private final String experience;
    private final Text profession;
    private final String percent;
    private final String item;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().replaceAll("§.", "").matches("^x\\d \\[\\+\\d+ .+ing XP](?:.|\\n)+");
    }

    public GatheringXPGain(Text text) {
        super(text);
        Matcher matcher = regex.matcher(text.getString());
        if(matcher.find()) {
            this.multiplier = matcher.group(1);
            this.experience = matcher.group(2);
            this.profession = Profession.getProfession(matcher.group(3).charAt(0)).getTextWithIcon().setStyle(GRAY);
            this.percent = matcher.group(4);
            this.item = matcher.group(5);
        }
        else throw new TextTranslationFailException("GatheringXPGain.class");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.professionXp";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(GRAY);
        resultText.append(multiplier);

        resultText.append("[");
        resultText.append(Text.translatable(translationKey, experience, profession).setStyle(GRAY));
        resultText.append("]");
        resultText.append(percent);
        if(item != null) {
            resultText.append("\n");
            resultText.append(item);
        }
    }
}
