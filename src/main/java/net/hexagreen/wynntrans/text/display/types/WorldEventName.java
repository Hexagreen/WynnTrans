package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class WorldEventName extends WynnDisplayText {
    private final String keyWorldEvent;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getStyle().equals(Style.EMPTY.withColor(0xEBF7FF));
    }

    public WorldEventName(Text text) {
        super(text);
        this.keyWorldEvent = "wytr.worldEvent." + normalizeStringForKey(getContentString());
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = newTranslate(keyWorldEvent).setStyle(Style.EMPTY.withColor(0xEBF7FF));
    }
}
