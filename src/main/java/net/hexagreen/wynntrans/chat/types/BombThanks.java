package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombThanks extends WynnChatText {
    private final Text playerName;

    public BombThanks(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = ((MutableText) getPlayerNameFromSibling(1)).setStyle(getStyle(0));
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "bombThanks";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, playerName).setStyle(getStyle(0)))
                .append(newTranslate(parentKey + ".click").setStyle(getStyle(3)));
    }
}
