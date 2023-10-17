package net.hexagreen.wynntrans.chat;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombThanks extends WynnChatText {
    private final Text playerName;

    protected BombThanks(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = ((MutableText) getPlayerNameFromSibling(1)).setStyle(getStyle(0));
    }

    public static BombThanks of(Text text, Pattern regex) {
        return new BombThanks(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "bombThanks";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "_1", playerName).setStyle(getStyle(0)))
                .append(newTranslate(parentKey + "_2").setStyle(getStyle(3)));
    }
}
