package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class AreaLeave extends WynnChatText {
    private final String areaName;

    protected AreaLeave(Text text, Pattern regex) {
        super(text, regex);
        this.areaName = matcher.group(1);
    }

    public static AreaLeave of(Text text, Pattern regex) {
        return new AreaLeave(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "area.leave";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, areaName).setStyle(getStyle(0)));
    }
}
