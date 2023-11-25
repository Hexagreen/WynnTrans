package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Disguise extends WynnChatText {
    public Disguise(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "disguise";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(0))
                .append(newTranslate(parentKey + "_1").setStyle(getStyle(1)))
                .append(getSibling(2))
                .append(newTranslate(parentKey + "_2").setStyle(getStyle(3)));
    }
}
