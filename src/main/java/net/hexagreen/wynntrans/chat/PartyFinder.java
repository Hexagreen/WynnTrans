package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class PartyFinder extends WynnChatText {
    private final Object playerName;
    private final String players;

    protected PartyFinder(Text text, Pattern regex) {
        super(text, regex);
        if(inputText.getSiblings().size() > 6) {
            this.playerName = getSibling(2);
        } else {
            this.playerName = matcher.group(1);
        }
        this.players = matcher.group(2);
    }

    public static PartyFinder of(Text text, Pattern regex) {
        return new PartyFinder(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "partyFinder";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)).append(": "))
                .append(newTranslate(parentKey + "_1", playerName).setStyle(getStyle(1)))
                .append(getSibling(2))
                .append(newTranslate(parentKey + "_2").setStyle(getStyle(3)))
                .append(newTranslate(parentKey + "_3", players).setStyle(getStyle(4)))
                .append(newTranslate(parentKey + "_4").setStyle(getStyle(5)));
    }
}
