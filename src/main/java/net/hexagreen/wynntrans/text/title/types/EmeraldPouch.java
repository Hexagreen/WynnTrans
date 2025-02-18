package net.hexagreen.wynntrans.text.title.types;

import net.hexagreen.wynntrans.text.title.WynnTitleText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class EmeraldPouch extends WynnTitleText {
    private final String count;

    public static boolean typeChecker(Text text) {
        return text.getString().matches("§a\\+\\d+§7 Emeralds? §ato pouch");
    }

    public EmeraldPouch(Text text) {
        super(text);
        this.count = getContentString().replaceAll("§.", "").replaceAll("\\D", "");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "title.emeraldPouch";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.translatable(translationKey, count).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
    }
}
