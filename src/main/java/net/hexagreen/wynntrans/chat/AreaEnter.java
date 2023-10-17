package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class AreaEnter extends WynnChatText {
    private final String areaName;

    protected AreaEnter(Text text, Pattern regex) {
        super(text, regex);
        this.areaName = matcher.group(1);
    }

    public static AreaEnter of(Text text, Pattern regex) {
        return new AreaEnter(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "area.enter";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, areaName).setStyle(getStyle(0)));
    }
}
