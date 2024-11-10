package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class BombThanked extends WynnChatText {
    private final String playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("said thank you for supporting Wynncraft!").matcher(text.getString()).find();
    }

    public BombThanked(Text text) {
        super(text);
        this.playerName = text.getString().replaceFirst("§7(.+) said", "$1");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.thanked";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.translatable(parentKey, playerName).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    }
}
