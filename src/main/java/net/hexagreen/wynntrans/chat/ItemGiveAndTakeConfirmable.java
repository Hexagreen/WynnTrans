package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ItemGiveAndTakeConfirmable extends ItemGiveAndTake implements IFocusText {
    private final Text fullText;

    protected ItemGiveAndTakeConfirmable(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.fullText = text;
    }

    public static ItemGiveAndTakeConfirmable of(Text text, Pattern regex) {
        return new ItemGiveAndTakeConfirmable(text, regex);
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
