package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class DeathItemLost extends WynnChatText {
    private final Text lostItems;

    public DeathItemLost(Text text, Pattern regex) {
        super(text, regex);
        this.lostItems = initLost();
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.itemLost";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, lostItems).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
    }

    private Text initLost() {
        MutableText rewards = Text.empty();
        for(int i = 1; i < getSiblings().size(); i++) {
            rewards.append("\n").append(getSibling(i));
        }
        return rewards;
    }
}