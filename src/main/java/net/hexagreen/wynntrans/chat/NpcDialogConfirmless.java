package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NpcDialogConfirmless extends NpcDialog implements IFocusText {
    public NpcDialogConfirmless(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
    }

    @Override
    protected void build() {
        super.build();
        resultText = setToConfirmless(resultText);
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
