package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.IFocusText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NewQuestFocused extends NewQuest implements IFocusText {
    private final Text fullText;
    private final FocusType focusType;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(?:New |Mini-)?Quest(?:line)? Started: ").matcher(text.getString()).find();
    }

    public NewQuestFocused(Text text) {
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
