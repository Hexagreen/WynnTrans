package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.IFocusText;
import net.minecraft.text.Text;

public class ItemGiveAndTakeFocused extends ItemGiveAndTake implements IFocusText {
    private final Text fullText;
    private final FocusType focusType;

    public ItemGiveAndTakeFocused(Text text) {
        super(text.getSiblings().get(2));
        this.fullText = text;
        this.focusType = detectFocusType(text);
    }

    @Override
    protected void build() {
        super.build();
        switch(focusType) {
            case PRESS_SHIFT -> resultText = setToPressShift(resultText, fullText);
            case CUTSCENE -> resultText = setToCutScene(resultText);
            case AUTO -> resultText = setToConfirmless(resultText);
        }
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
