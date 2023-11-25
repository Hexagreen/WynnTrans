package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NpcDialogConfirmable extends NpcDialog implements IFocusText {
    private final Text fullText;

    public NpcDialogConfirmable(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.fullText = text;
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
