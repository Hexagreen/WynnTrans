package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CharacterClass;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class FriendJoin extends WynnChatText {
    private final Text playerName;
    private final Text worldChannel;
    private final Text charClass;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^.+ has logged into server WC\\d+ as an? (.+)$").matcher(text.getString()).find();
    }

    public FriendJoin(Text text) {
        super(text, Pattern.compile("^.+ has logged into server WC\\d+ as an? (.+)$"));
        this.playerName = getSibling(0);
        this.worldChannel = getSibling(2);
        this.charClass = CharacterClass.getClassName(matcher.group(1)).setStyle(getStyle(4));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.friendJoin";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, playerName, worldChannel, charClass));
    }
}
