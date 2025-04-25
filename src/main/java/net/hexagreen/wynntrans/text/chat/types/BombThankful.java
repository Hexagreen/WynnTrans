package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombThankful extends WynnChatText {
    private final MutableText playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§7You have thanked (.+)$").matcher(text.getString()).find();
    }

    public BombThankful(Text text) {
        super(text);
        if(text.getSiblings().size() == 1) {
            this.playerName = (MutableText) getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = (MutableText) Text.of(inputText.getString().replaceFirst(".+thanked (.+)$", "$1"));
        }
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.thankyou";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, playerName).setStyle(GRAY));
    }
}
