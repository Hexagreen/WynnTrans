package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class FriendLeft extends WynnChatText {
    private final Text playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§a(.+) left the game\\.$").matcher(text.getString()).find();
    }

    public FriendLeft(Text text) {
        super(text);
        if(text.getSiblings().size() > 1) {
            this.playerName = getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = Text.literal(inputText.getString().replaceFirst("^§a(.+) left.+", "$1"));
        }
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.friendLeft";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(Text.translatable(translationKey, playerName).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    }
}
