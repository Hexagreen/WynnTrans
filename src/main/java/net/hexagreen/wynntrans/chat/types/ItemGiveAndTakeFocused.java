package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ItemGiveAndTakeFocused extends ItemGiveAndTake implements IFocusText {
    private final Text fullText;
    private final FocusType focusType;

    public ItemGiveAndTakeFocused(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
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
