package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ItemGiveAndTake extends WynnChatText {
    private final String direction;
    private final String number;
    private final String item;

    protected ItemGiveAndTake(Text text, Pattern regex) {
        super(text, regex);
        this.direction = matcher.group(1);
        this.number = matcher.group(2);
        this.item = matcher.group(3);
    }

    public static ItemGiveAndTake of(Text text, Pattern regex) {
        return new ItemGiveAndTake(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "item";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(getStyle(0));
        resultText.append("[" + direction + number + " " + item + "]");
    }
}
