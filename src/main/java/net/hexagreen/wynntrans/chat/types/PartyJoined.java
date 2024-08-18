package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class PartyJoined extends WynnChatText {
    private final Text playerName;

    public PartyJoined(Text text, Pattern regex) {
        super(text, regex);
        if(text.getSiblings().size() > 1) {
            this.playerName = getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = Text.literal(matcher.group(1)).setStyle(getStyle(0));
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.partyJoined";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(newTranslate(parentKey, playerName).setStyle(getStyle(0)));
    }
}
