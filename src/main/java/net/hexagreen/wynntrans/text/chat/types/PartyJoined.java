package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class PartyJoined extends WynnChatText {
    private final Text playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(.+) has joined your party, say hello!$").matcher(text.getString()).find();
    }

    public PartyJoined(Text text) {
        super(text, Pattern.compile("^(.+) has joined your party, say hello!$"));
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

        resultText.append(Text.translatable(parentKey, playerName).setStyle(getStyle(0)));
    }
}
