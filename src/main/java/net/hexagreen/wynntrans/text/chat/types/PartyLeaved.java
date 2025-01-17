package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PartyLeaved extends WynnSystemText {
    private final Text playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(.+) has left the party.$").matcher(text.getString()).find();
    }

    public PartyLeaved(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("^(.+) has left the party.$").matcher(inputText.getString());
        boolean ignore = matcher.find();
        if(text.getSiblings().size() > 3) {
            this.playerName = getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = getSibling(2);
        }
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.partyLeaved";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty().setStyle(getStyle()).append(header);

        resultText.append(Text.translatable(translationKey, playerName));
    }
}
