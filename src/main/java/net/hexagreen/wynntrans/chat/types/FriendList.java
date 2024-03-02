package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class FriendList extends WynnChatText {
    private final String playerName;
    private final String friendNumber;

    public FriendList(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = matcher.group(1);
        this.friendNumber = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "friendList";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, playerName, friendNumber).setStyle(getStyle(0)));
        resultText.append(getSibling(1));
    }
}
