package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NarrationSelection extends Narration implements IFocusText {
    private final Text fullText;

    public NarrationSelection(Text text, Pattern ignore) {
        super(text.getSiblings().get(2), null);
        this.fullText = text;
    }

    @Override
    protected void build() {
        super.build();
        resultText = setToSelectOption(resultText, fullText, pKeyNarration);
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
