package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class BombThankful extends WynnChatText {
    private final MutableText playerName;

    public BombThankful(Text text, Pattern regex) {
        super(text, regex);
        if(text.getSiblings().size() == 1) {
            this.playerName = (MutableText) getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = (MutableText) Text.of(matcher.group(1));
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.thankyou";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, playerName).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
