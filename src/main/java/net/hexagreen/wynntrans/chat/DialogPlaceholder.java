package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DialogPlaceholder extends WynnChatText implements IFocusText {
    protected DialogPlaceholder(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
    }

    public static DialogPlaceholder of(Text text, Pattern regex) {
        return new DialogPlaceholder(text, regex);
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n").append("\n").append("\n").append("\n").append("\n");
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
