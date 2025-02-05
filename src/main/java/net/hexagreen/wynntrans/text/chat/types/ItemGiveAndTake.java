package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemGiveAndTake extends WynnChatText {
    private final String direction;
    private final String number;
    private final String item;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§.\\[([+-])(\\d+) (.+)]$").matcher(text.getString()).find();
    }

    public ItemGiveAndTake(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("^§.\\[([+-])(\\d+) (.+)]$").matcher(inputText.getString());
        boolean ignore = matcher.find();
        this.direction = matcher.group(1);
        this.number = matcher.group(2);
        this.item = matcher.group(3);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(parseStyleCode(getContentString()));
        if(item.matches("\\W+")) {
            resultText.append("[" + direction + number + " " + item + "]");
        }
        else {
            resultText.append("[" + direction + number + " " + new ItemName(matcher.group(3)).textAsMutable().getString() + "]");
        }
    }
}
