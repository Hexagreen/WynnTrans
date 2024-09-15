package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class WorldEventTimer extends WynnDisplayText {
    private final Text timer;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("^Starts in \\d+[hms]");
    }

    public WorldEventTimer(Text text) {
        super(text);
        this.timer = ITime.translateTime(getContentString().replaceAll("Starts in ", ""));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.worldEventTimer";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = newTranslate(parentKey, timer).setStyle(Style.EMPTY.withColor(0xAEB8BF));
    }
}
