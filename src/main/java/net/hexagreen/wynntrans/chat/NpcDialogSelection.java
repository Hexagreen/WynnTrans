package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NpcDialogSelection extends NpcDialog implements IFocusText {
    private final Text fullText;

    protected NpcDialogSelection(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.fullText = text;
    }

    public static NpcDialogSelection of(Text text, Pattern regex) {
        return new NpcDialogSelection(text, regex);
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
