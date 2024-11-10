package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class UnusedStatPoint extends WynnChatText {
    private final String skillPoint;
    private final String abilityPoint;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§4You have (?:§c§l(\\d+) unused Skill Points?)?(?:§4 and )?(?:§b§l(\\d+) unused Ability Points?)?! §4Right-Click").matcher(text.getString()).find();
    }

    public UnusedStatPoint(Text text) {
        super(text, Pattern.compile("^§4You have (?:§c§l(\\d+) unused Skill Points?)?(?:§4 and )?(?:§b§l(\\d+) unused Ability Points?)?! §4Right-Click"));
        this.skillPoint = matcher.group(1);
        this.abilityPoint = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.statPointAlert";
    }

    @Override
    protected void build() {
        Style style = Style.EMPTY.withColor(Formatting.DARK_RED);

        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey + ".1").setStyle(style));
        if(skillPoint != null) {
            resultText.append(Text.translatable(parentKey + ".sp", skillPoint).setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)));
            if(abilityPoint != null) {
                resultText.append(Text.translatable(parentKey + ".2").setStyle(style)).append(Text.translatable(parentKey + ".ap", abilityPoint).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true)));
            }
        }
        else {
            resultText.append(Text.translatable(parentKey + ".ap", abilityPoint).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true)));
        }
        resultText.append(Text.translatable(parentKey + ".3").setStyle(style));
    }
}
