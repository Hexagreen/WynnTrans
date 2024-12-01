package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Disguise extends WynnChatText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^.+ has disguised as a .+!").matcher(text.getString()).find();
    }

    public Disguise(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.disguise";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, getSibling(0), getSibling(2)).setStyle(getStyle(1)));
    }
}
