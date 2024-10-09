package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DamageIndicator extends WynnDisplayText {
    private static final Pattern indicator = Pattern.compile("^(?:§e(?:§l)?-\\d+ ✦ |§c(?:§l)?-\\d+ ✹ |§4(?:§l)?-\\d+ ❤ |§f(?:§l)?-\\d+ ❋ |§b(?:§l)?-\\d+ ❉ |§2(?:§l)?-\\d+ ✤ )+$");
    private final boolean dodge;

    public static boolean typeChecker(Text text) {
        return (text.getString().equals("Dodged") || indicator.matcher(text.getString()).matches());
    }

    public DamageIndicator(Text text) {
        super(text);
        this.dodge = text.getString().equals("Dodged");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.dodged";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(dodge) resultText = Text.translatable(parentKey);
        else resultText = inputText;
    }
}
