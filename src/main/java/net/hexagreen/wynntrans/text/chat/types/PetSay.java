package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

public class PetSay extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        if(text.getString().contains("\uDAFF\uDFFC\uE016\uDAFF\uDFFF\uE002\uDAFF\uDFFE")) return true;
        return text.getString().contains("\uDAFF\uDFFC\uE001\uDB00\uDC06")
                && text.getStyle().getColor() != null
                && text.getStyle().getColor().getHexCode().equals("#FFAA00");
    }

    public PetSay(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = inputText;
    }
}
