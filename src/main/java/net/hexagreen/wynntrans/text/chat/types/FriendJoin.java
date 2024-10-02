package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CharacterClass;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class FriendJoin extends WynnChatText {
    private final Text playerName;
    private final Text worldChannel;
    private final Text charClass;

    public FriendJoin(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = getSibling(0);
        this.worldChannel = getSibling(2);
        this.charClass = CharacterClass.getClassName(matcher.group(1)).setStyle(getStyle(4));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.friendJoin";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, playerName, worldChannel, charClass));
    }
}
