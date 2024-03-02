package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class PartyFinder extends WynnChatText {
    private final Object playerName;
    private final String players;

    public PartyFinder(Text text, Pattern regex) {
        super(text, regex);
        if(inputText.getSiblings().size() > 6) {
            this.playerName = getSibling(2);
        } else {
            this.playerName = matcher.group(1);
        }
        this.players = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "partyFinder";
    }

    @Override
    protected void build() {
        Text standingPlayers = Text.empty()
                .append(newTranslate(parentKey + ".players", players).setStyle(getStyle(4)));

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)).append(": "))
                .append(newTranslate(parentKey + ".message", playerName, getSibling(2), standingPlayers).setStyle(getStyle(1)));
    }
}
