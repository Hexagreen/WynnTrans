package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Disguise extends WynnChatText {
    public Disguise(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.disguise";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, getSibling(0), getSibling(2)).setStyle(getStyle(1)));
    }
}
