package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.IFocusText;
import net.minecraft.text.Text;

public class MiniQuestInfoFocused extends MiniQuestInfo implements IFocusText {
    private final Text fullText;

    public MiniQuestInfoFocused(Text text) {
        super(text.getSiblings().get(2));
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
