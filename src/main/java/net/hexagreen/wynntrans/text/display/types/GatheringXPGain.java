package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class GatheringXPGain extends WynnDisplayText {
    private static final Pattern regex = Pattern.compile("^(§.x\\d )§.\\[\\+(§.\\d+)§. (.)§7 .+ XP]( .+)(?:\\n(§..+))?");

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().isEmpty()) return false;
        return text.getString().replaceAll("§.", "").matches("^\\+\\d+ . .+ing XP \\[.+%](?:.|\\n)*");
    }

    public GatheringXPGain(Text text) {
        super(flatText(text));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.professionXp";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text experience = Text.literal(getContentString().trim()).setStyle(getStyle());
        Text profession = Profession.getProfession(getContentString(0).replaceAll("^. | XP ", ""))
                .getTextWithIcon().setStyle(getStyle(0));
        Text percent = Text.literal(getContentString(1).replaceAll("\\n", ""))
                .setStyle(getStyle(1));
        Text itemAmount = getSiblings().size() >= 4 ? Text.literal("\n" + getContentString(2).trim())
                .setStyle(getStyle(2)) : Text.empty();
        Text item = getSiblings().size() >= 4 ? new ItemName(getSibling(3)).textAsMutable() : Text.empty();

        resultText = Text.translatable(translationKey, experience, profession, percent, itemAmount, item).setStyle(GRAY);
    }
}
