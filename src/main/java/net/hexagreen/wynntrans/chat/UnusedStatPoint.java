package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class UnusedStatPoint extends WynnChatText {
    private final String skillPoint;
    private final String abilityPoint;

    protected UnusedStatPoint(Text text, Pattern regex) {
        super(text, regex);
        this.skillPoint = matcher.group(1);
        this.abilityPoint = matcher.group(2);
    }

    public static UnusedStatPoint of(Text text, Pattern regex) {
        return new UnusedStatPoint(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "statPointAlert";
    }

    @Override
    protected void build() {
        Style style = Style.EMPTY.withColor(Formatting.DARK_RED);

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "_1").setStyle(style));
        if(skillPoint != null) {
            resultText.append(newTranslate(parentKey + ".sp", skillPoint).setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)));
            if(abilityPoint != null) {
                resultText.append(newTranslate(parentKey + "_2").setStyle(style))
                        .append(newTranslate(parentKey + ".ap", abilityPoint).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true)));
            }
        }
        else {
            resultText.append(newTranslate(parentKey + ".ap", abilityPoint).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true)));
        }
        resultText.append(newTranslate(parentKey + "_3").setStyle(style));
    }
}
