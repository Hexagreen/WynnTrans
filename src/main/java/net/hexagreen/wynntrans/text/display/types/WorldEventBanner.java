package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class WorldEventBanner extends WynnDisplayText {
    public static boolean typeChecker(Text text) {
        return text.getString().contains("\uE016\uE00E\uE011\uE00B\uE003 \uE004\uE015\uE004\uE00D\uE013\uDB00\uDC02");
    }

    public WorldEventBanner(Text text) {
        super(text);
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = inputText;
    }
}
