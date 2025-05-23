package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class PartyFinder extends WynnSystemText {
    private final String playerName;
    private final Text partyName;
    private final Text players;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() < 2) return false;
        return text.getSiblings().get(1).getString().equals(" Party Finder:");
    }

    public PartyFinder(Text text) {
        super(text);
        this.playerName = getContentString(1).replaceFirst("(?s)Hey (.+),.+", "$1");
        this.partyName = getSibling(2);
        this.players = getSibling(4);
    }

    @Override
    protected int setLineWrappingWidth() {
        return (int) ISpaceProvider.CHAT_HUD_WIDTH / 2;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.partyFinder";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)).append(": "))
                .append(Text.translatable(translationKey + ".message", playerName, partyName, players).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }
}
