package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombThanks extends WynnChatText {
    private final Text playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^Want to thank .+\\? Click here to thank them!$").matcher(text.getString()).find();
    }

    public BombThanks(Text text) {
        super(text);
        this.playerName = ((MutableText) getPlayerNameFromSibling(0)).setStyle(getStyle(0));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.bombThanks";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, playerName).setStyle(getStyle())).append(Text.translatable(parentKey + ".click").setStyle(getStyle(2)));
    }
}
