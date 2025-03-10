package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ArcherTrap extends WynnDisplayText {
    private final boolean arming;
    private final String damage;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("§b\\+\\d+% §7Damage") || text.getString().equals("§eArming...");
    }

    public ArcherTrap(Text text) {
        super(text);
        this.arming = !getContentString().contains("+");
        this.damage = getContentString().replaceFirst(" §7Damage", "");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.archerTrap";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(arming) resultText = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
        else
            resultText = Text.translatable(translationKey + ".charge", damage).setStyle(Style.EMPTY.withColor(Formatting.GRAY));

    }
}
