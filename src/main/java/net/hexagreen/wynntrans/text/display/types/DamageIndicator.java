package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DamageIndicator extends WynnDisplayText {
    private static final Pattern indicator = Pattern.compile("^(?:(?:§l)?-\\d+ [\uE000-\uE005] |§4(?:§l)?-\\d+ ❤ |§5-\\d+ ☠ )+$");
    private final boolean dodge;

    public static boolean typeChecker(Text text) {
        return (text.getString().equals("Dodged") || indicator.matcher(text.getString()).matches());
    }

    public DamageIndicator(Text text) {
        super(text);
        this.dodge = text.getString().equals("Dodged");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.dodged";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(dodge) resultText = Text.translatable(translationKey);
        else resultText = inputText;
    }
}
