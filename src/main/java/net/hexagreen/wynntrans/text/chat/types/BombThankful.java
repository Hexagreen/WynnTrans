package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombThankful extends WynnSystemText {
    private final MutableText playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("You have thanked (.+)$").matcher(text.getString()).find();
    }

    public BombThankful(Text text) {
        super(text);
        this.playerName = Text.literal(inputText.getString().replaceFirst(".+thanked (.+)$", "$1"));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.thankyou";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(Text.translatable(translationKey, playerName));
    }
}
