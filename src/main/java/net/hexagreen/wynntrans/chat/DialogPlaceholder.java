package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DialogPlaceholder extends WynnChatText implements IFocusText {
    public DialogPlaceholder(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
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
