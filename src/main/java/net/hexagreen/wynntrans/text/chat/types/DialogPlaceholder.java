package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.IFocusText;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

public class DialogPlaceholder extends WynnChatText implements IFocusText {
    private final int lines;

    public DialogPlaceholder(Text text) {
        super(text.getSiblings().get(2));
        this.lines = text.getString().length() - text.getString().replace("\n", "").length();
    }

    @Override
    protected String setTranslationKey() {
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
