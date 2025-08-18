package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class PetName extends WynnDisplayText {
    public static boolean typeChecker(Text text) {
        if(text.getSiblings().isEmpty()) return false;
        return text.getString().contains("\uE00F\uE004\uE013\uDB00\uDC02");
    }

    public PetName(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = inputText;
    }
}
