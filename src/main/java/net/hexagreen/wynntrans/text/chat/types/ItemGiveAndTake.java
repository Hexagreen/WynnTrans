package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ItemGiveAndTake extends WynnChatText {
    private final String direction;
    private final String number;
    private final String item;

    public ItemGiveAndTake(Text text, Pattern regex) {
        super(text, regex);
        this.direction = matcher.group(1);
        this.number = matcher.group(2);
        this.item = matcher.group(3);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.item";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(
                parseStyleCode(getContentString())
        );
        resultText.append("[" + direction + number + " " + item + "]");
    }
}
