package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.IFocusText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NpcDialogFocused extends NpcDialog implements IFocusText {
    private final Text fullText;
    private final FocusType focusType;

    public NpcDialogFocused(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.fullText = text;
        this.focusType = detectFocusType(text);
    }

    @Override
    protected void build() {
        super.build();
        switch(focusType) {
            case PRESS_SHIFT -> resultText = setToPressShift(resultText, fullText);
            case SELECT_OPTION -> resultText = setToSelectOption(resultText, fullText, keyDialog);
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
