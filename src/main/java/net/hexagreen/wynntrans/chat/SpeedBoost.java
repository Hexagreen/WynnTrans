package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class SpeedBoost extends WynnChatText {
    private final String duration;

    protected SpeedBoost(Text text, Pattern regex) {
        super(text, regex);
        this.duration = matcher.group(1);
    }

    public static SpeedBoost of(Text text, Pattern regex) {
        return new SpeedBoost(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "speedboost";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "_pre").setStyle(getStyle(1)))
                .append(newTranslate(parentKey, duration))
                .append(newTranslate(parentKey + "_suf").setStyle(getStyle(1)));
    }
}
