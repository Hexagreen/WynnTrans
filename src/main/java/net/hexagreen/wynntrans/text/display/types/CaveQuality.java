package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class CaveQuality extends WynnDisplayText {
    private final String bar;

    public static boolean typeChecker(Text text) {
        return text.getString().matches("^§7Loot Quality\\n.+");
    }

    public CaveQuality(Text text) {
        super(text);
        this.bar = text.getString().split("\\n")[1];
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.caveQuality";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey).setStyle(GRAY)).append("\n").append(bar);
    }
}
