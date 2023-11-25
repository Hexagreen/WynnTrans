package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class SpeedBoost extends WynnChatText {
    private final String duration;

    public SpeedBoost(Text text, Pattern regex) {
        super(text, regex);
        this.duration = matcher.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "speedboost";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "_pre").setStyle(getStyle(1)))
                .append(newTranslate(parentKey, duration).setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
                .append(newTranslate(parentKey + "_suf").setStyle(getStyle(1)));
    }
}
