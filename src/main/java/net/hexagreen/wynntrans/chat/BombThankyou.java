package net.hexagreen.wynntrans.chat;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class BombThankyou extends WynnChatText {
    private final MutableText playerName;

    public BombThankyou(Text text, Pattern regex) {
        super(text, regex);
        if(text.getSiblings().size() == 2) {
            this.playerName = (MutableText) getPlayerNameFromSibling(1);
        }
        else {
            this.playerName = (MutableText) Text.of(matcher.group(1));
        }
        this.playerName.setStyle(Style.EMPTY.withColor(Formatting.AQUA));
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "thankyou";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "_pre"))
                .append(playerName)
                .append(newTranslate(parentKey + "_suf").setStyle(getStyle(0)));
    }
}
