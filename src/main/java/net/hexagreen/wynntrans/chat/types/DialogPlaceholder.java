package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DialogPlaceholder extends WynnChatText implements IFocusText {
    private final int lines;

    public DialogPlaceholder(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.lines = text.getString().length() - text.getString().replace("\n", "").length();
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        for(int i = lines; i > 0; i--) {
            resultText.append("\n");
        }
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
