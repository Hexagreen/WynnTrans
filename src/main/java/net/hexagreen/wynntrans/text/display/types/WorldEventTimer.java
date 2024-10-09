package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class WorldEventTimer extends WynnDisplayText {
    private final boolean startTimer;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("^Starts in \\d+[hms]|^\\d+ targets? remains?");
    }

    public WorldEventTimer(Text text) {
        super(text);
        this.startTimer = !getContentString().contains("target");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.worldEvent.timer";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Style style = Style.EMPTY.withColor(0xAEB8BF);
        if(startTimer) {
            Text timer = ITime.translateTime(getContentString().replaceAll("Starts in ", ""));
            resultText = Text.translatable(parentKey, timer).setStyle(style);
        }
        else {
            String num = getContentString().replaceAll("\\D", "");
            resultText = Text.translatable("wytr.display.worldEvent.leftTarget", num).setStyle(style);
        }
    }
}
