package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Resistance extends WynnChatText {
    public Resistance(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "resistance";
    }

    @Override
    protected void build() {
        MutableText buffs = Text.empty()
                .append(newTranslate(parentKey + ".res").setStyle(getStyle(2)));
        if(inputText.getSiblings().size() == 6) {
            buffs.append(newTranslate(parentKey + ".and").setStyle(getStyle(3)))
                    .append(newTranslate(parentKey + ".str").setStyle(getStyle(4)));
        }

        resultText = Text.empty();

        resultText.append(newTranslate(parentKey, getSibling(0), buffs).setStyle(getStyle(1)));
    }
}
