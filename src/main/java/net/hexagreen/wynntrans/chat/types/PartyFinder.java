package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class PartyFinder extends WynnChatText {
    private final String playerName;
    private final String partyName;
    private final String players;

    public PartyFinder(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = matcher.group(1);
        this.partyName = matcher.group(2);
        this.players = matcher.group(3);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.partyFinder";
    }

    @Override
    protected void build() {
        Text standingPlayers = Text.empty()
                .append(newTranslate(parentKey + ".players", players).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)).append(": "))
                .append(newTranslate(parentKey + ".message", playerName, partyName, standingPlayers).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }
}
