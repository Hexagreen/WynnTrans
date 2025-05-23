package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class CrateOpening extends WynnDisplayText {
    private final Text crateName;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("^.+Crate.+\\n§7Click to Open");
    }

    public CrateOpening(Text text) {
        super(text);
        this.crateName = new SimpleDisplay(Text.literal(text.getString().split("\\n")[0])).text();
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.crateOpen";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(crateName).append("\n").append(Text.translatable(translationKey).setStyle(GRAY));
    }
}
