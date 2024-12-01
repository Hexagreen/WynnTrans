package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class FriendAdded extends WynnChatText {
    private final String playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(.+) has added you as a friend!$").matcher(text.getString()).find();
    }

    public FriendAdded(Text text) {
        super(text, Pattern.compile("^(.+) has added you as a friend!$"));
        this.playerName = matcher.group(1);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.friendAdded";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, playerName).setStyle(getStyle(0)));
    }
}
