package net.hexagreen.wynntrans.text.title.types;

import net.hexagreen.wynntrans.text.title.WynnTitleText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class AreaUnlocked extends WynnTitleText {

    public static boolean typeChecker(Text text) {
        String string = text.getString().replaceAll("§.", "");
        return WTS.checkTranslationDoNotRegister("wytr.area." + normalizeStringForKey(string));
    }

    public AreaUnlocked(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Style style = parseStyleCode(getContentString());
        resultText = Text.translatable("wytr.area." + normalizeStringForKey(getContentString().replaceAll("§.", ""))).setStyle(style);
    }
}
