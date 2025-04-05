package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GatheringToolLevelRequirement extends WynnSystemText {

    public static final Pattern REGEX = Pattern.compile(" You need a §f([ⒷⒸⒿⓀ])§c .+§4 level of (§c\\d+)§4 to use this tool!");

    public static boolean typeChecker(Text text) {
        return REGEX.matcher(text.getString()).find();
    }

    public GatheringToolLevelRequirement(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.toolLevelReq";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Matcher matcher = REGEX.matcher(inputText.getString());
        if(matcher.find()) {
            Profession profession = Profession.getProfession(matcher.group(1));
            Text prof = profession.getIcon().setStyle(Style.EMPTY.withColor(Formatting.WHITE))
                    .append(profession.getIcon().setStyle(Style.EMPTY.withColor(Formatting.RED)));
            String level = matcher.group(2);
            resultText = Text.translatable(translationKey, prof, level).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
        }
        else {
            resultText = inputText;
        }
    }
}
