package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Shout extends WynnChatText {
    private final String name;
    private final String server;

    protected Shout(Text text, Pattern regex) {
        super(text, regex);
        this.name = matcher.group(1);
        this.server = matcher.group(2);
    }

    public static Shout of(Text text, Pattern regex) {
        return new Shout(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "shout";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, name, server).setStyle(getStyle(0)));
        resultText.append(inputText.getSiblings().get(1));
    }
}
