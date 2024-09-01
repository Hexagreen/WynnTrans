package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class CrateReward extends WynnDisplayText {

    public static boolean typeChecker(Text text) {
        return false;
    }

    public CrateReward(Text text) {
        super(text);
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {

    }
}
