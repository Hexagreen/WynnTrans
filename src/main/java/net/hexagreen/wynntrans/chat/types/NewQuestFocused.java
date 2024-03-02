package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.IFocusText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NewQuestFocused extends NewQuest implements IFocusText {
    private final Text fullText;
    private final FocusType focusType;

    public NewQuestFocused(Text text, Pattern regex) {
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
