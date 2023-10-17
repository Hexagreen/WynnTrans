package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Resistance extends WynnChatText {
    protected Resistance(Text text, Pattern regex) {
        super(text, regex);
    }

    public static Resistance of(Text text, Pattern regex) {
        return new Resistance(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "resistance";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(0))
                .append(newTranslate(parentKey + "_1").setStyle(getStyle(1)))
                .append(newTranslate(parentKey + "_2").setStyle(getStyle(2)));
        if(inputText.getSiblings().size() == 6) {
            resultText.append(newTranslate(parentKey + "_3").setStyle(getStyle(3)))
                    .append(newTranslate(parentKey + "_4").setStyle(getStyle(4)));
        }
        resultText.append(newTranslate(parentKey + "_5").setStyle(getStyle(1)));
    }
}
