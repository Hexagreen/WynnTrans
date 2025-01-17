package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class PartyFinder extends WynnChatText {
    private final String playerName;
    private final String partyName;
    private final String players;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§5Party Finder:§d Hey (.+), over here! Join the (§..+)§d queue and match up with §e(\\d+) other players?§d!$").matcher(text.getString()).find();
    }

    public PartyFinder(Text text) {
        super(text);
        this.playerName = inputText.getString().replaceFirst(".+Hey (.+), .+", "$1");
        this.partyName = inputText.getString().replaceFirst(".+Join the (§..+)§d queue.+", "$1");
        this.players = inputText.getString().replaceFirst(".+with §e(\\d+) other.+", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.partyFinder";
    }

    @Override
    protected void build() {
        Text standingPlayers = Text.empty().append(Text.translatable(translationKey + ".players", players).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));

        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)).append(": "))
                .append(Text.translatable(translationKey + ".message", playerName, partyName, standingPlayers).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }
}
