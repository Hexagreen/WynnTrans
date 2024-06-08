package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class PowderMaster extends WynnChatText {

    public PowderMaster(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "powderMaster";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)).append(": "));
        resultText.append(newTranslate(parentKey + ".confirm").setStyle(getStyle(1)));
    }
}
