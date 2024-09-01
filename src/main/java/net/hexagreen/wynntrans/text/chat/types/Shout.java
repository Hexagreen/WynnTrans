package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class Shout extends WynnChatText {
    private final String name;
    private final String server;

    public Shout(Text text, Pattern regex) {
        super(text, regex);
        this.name = matcher.group(1);
        this.server = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.shout";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, name, server).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)));
        for(int i = 1; i < inputText.getSiblings().size(); i++) {
            resultText.append(getSibling(i));
        }
    }
}
