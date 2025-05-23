package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class FriendAdd extends WynnChatText {
    private final String playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(.+) has been added to your friends!$").matcher(text.getString()).find();
    }

    public FriendAdd(Text text) {
        super(text);
        this.playerName = inputText.getString().replaceFirst("^(.+) has been.+", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.friendAdd";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, playerName).setStyle(getStyle(0)));
    }
}
