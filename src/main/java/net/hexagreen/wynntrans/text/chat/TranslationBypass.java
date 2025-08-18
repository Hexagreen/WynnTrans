package net.hexagreen.wynntrans.text.chat;

import net.minecraft.text.Text;

public class TranslationBypass extends WynnChatText {

    public static boolean typeChecker(Text text) {
        if(text.getString().matches("§c\\[\\+\\d+ ❤]")) return true;
        return false;
    }


    public TranslationBypass(Text text) {
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
