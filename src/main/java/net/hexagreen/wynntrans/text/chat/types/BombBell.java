package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Bombs;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class BombBell extends WynnChatText {
    private final Text playerName;
    private final Bombs bomb;
    private final Text channel;

    public BombBell(Text text, Pattern regex) {
        super(text, regex);
        if(getSibling(1).getString().isEmpty()) {
            this.playerName = getSibling(2);
        }
        else {
            this.playerName = Text.literal(getSibling(1).getString().replace(" ", "")).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
        }
        this.bomb = Bombs.findBomb(getSibling(-3).getString());
        this.channel = getSibling(-1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.bombBell";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(bomb == null) throw new TextTranslationFailException("BombBell.java");
        Text bombName = bomb.getBombName().setStyle(Style.EMPTY.withColor(Formatting.WHITE));

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, playerName, bombName, channel).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
