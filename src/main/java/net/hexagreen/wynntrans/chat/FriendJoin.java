package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.enums.CharacterClass;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class FriendJoin extends WynnChatText {
    private final Text playerName;
    private final Text worldChannel;
    private final Text charClass;

    protected FriendJoin(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = getSibling(0);
        this.worldChannel = getSibling(2);
        this.charClass = CharacterClass.getClassName(matcher.group(1)).setStyle(getStyle(4));
    }

    public static FriendJoin of(Text text, Pattern regex) {
        return new FriendJoin(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "friendJoin";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, playerName, worldChannel, charClass));
    }
}
