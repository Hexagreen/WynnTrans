package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnusedStatPoint extends WynnChatText {
    private final String skillPoint;
    private final String abilityPoint;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§4You have (?:§c§l(\\d+) unused Skill Points?)?(?:§4 and )?(?:§b§l(\\d+) unused Ability Points?)?! §4Right-Click").matcher(text.getString()).find();
    }

    public UnusedStatPoint(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("^§4You have (?:§c§l(\\d+) unused Skill Points?)?(?:§4 and )?(?:§b§l(\\d+) unused Ability Points?)?!.+").matcher(inputText.getString());
        boolean ignore = matcher.find();
        this.skillPoint = matcher.group(1);
        this.abilityPoint = matcher.group(2);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.statPointAlert";
    }

    @Override
    protected void build() {
        Style style = Style.EMPTY.withColor(Formatting.DARK_RED);

        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey + ".1").setStyle(style));
        if(skillPoint != null) {
            resultText.append(Text.translatable(translationKey + ".sp", skillPoint).setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)));
            if(abilityPoint != null) {
                resultText.append(Text.translatable(translationKey + ".2").setStyle(style)).append(Text.translatable(translationKey + ".ap", abilityPoint).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true)));
            }
        }
        else {
            resultText.append(Text.translatable(translationKey + ".ap", abilityPoint).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true)));
        }
        resultText.append(Text.translatable(translationKey + ".3").setStyle(style));
    }
}
