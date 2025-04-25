package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class WorldEventName extends WynnDisplayText {
    private final String keyWorldEvent;
    private final String valWorldEvent;
    private final boolean timerMode;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        if(text.getString().equals("Prepare to start")) return false;
        return text.getStyle().equals(Style.EMPTY.withColor(0xEBF7FF));
    }

    public WorldEventName(Text text) {
        super(text);
        this.valWorldEvent = getContentString();
        this.keyWorldEvent = "wytr.worldEvent." + normalizeStringForKey(valWorldEvent);
        this.timerMode = valWorldEvent.matches("\\d+ (hour|minute|second)s? left");
    }

    @Override
    protected String setTranslationKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Style style = Style.EMPTY.withColor(0xEBF7FF);
        if(!timerMode) {
            resultText = Text.translatableWithFallback(keyWorldEvent, valWorldEvent).setStyle(style);
        }
        else {
            Text time = ITime.translateTime(valWorldEvent.replaceFirst(" left", ""));
            resultText = Text.translatable("wytr.display.worldEvent.timeLeft", time).setStyle(style);
        }
    }
}
