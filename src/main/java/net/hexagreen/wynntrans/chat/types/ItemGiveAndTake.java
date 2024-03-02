package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.enums.ChatType;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

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
        return rootKey + dirFunctional + "item";
    }

    @Override
    protected void build() {
        if(!inputText.getContent().equals(TextContent.EMPTY)) {
            processMalformedDialog();
            return;
        }
        resultText = Text.empty().setStyle(getStyle(0));
        resultText.append("[" + direction + number + " " + item + "]");
    }

    private void processMalformedDialog() {
        MutableText corrected = Text.empty().append(inputText);
        resultText = new ItemGiveAndTake(corrected, ChatType.DIALOG_ITEM.getRegex()).text();
    }
}
