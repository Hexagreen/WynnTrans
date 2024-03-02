package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DeathItemLost extends WynnChatText {
    private final Text lostItems;

    public DeathItemLost(Text text, Pattern regex) {
        super(text, regex);
        this.lostItems = getLost();
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "itemLost";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, lostItems).setStyle(getSibling(0).getSiblings().get(0).getStyle()));
    }

    private Text getLost() {
        MutableText rewards = Text.empty();
        for(int i = 1; i < inputText.getSiblings().size(); i++) {
            rewards.append("\n").append(getSibling(i));
        }
        return rewards;
    }
}