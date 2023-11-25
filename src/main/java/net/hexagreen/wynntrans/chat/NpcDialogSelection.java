package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NpcDialogSelection extends NpcDialog implements IFocusText {
    private final Text fullText;

    public NpcDialogSelection(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.fullText = text;
    }

    @Override
    protected void build() {
        super.build();
        resultText = setToSelectOption(resultText, fullText, pKeyDialog);
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
