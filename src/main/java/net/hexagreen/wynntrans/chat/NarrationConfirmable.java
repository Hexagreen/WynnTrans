package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NarrationConfirmable extends Narration implements IFocusText {
    private final Text fullText;

    protected NarrationConfirmable(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.fullText = text;
    }

    public static NarrationConfirmable of(Text text, Pattern regex) {
        return new NarrationConfirmable(text, regex);
    }

    @Override
    protected void build() {
        super.build();
        resultText = setToPressShift(resultText, fullText);
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
