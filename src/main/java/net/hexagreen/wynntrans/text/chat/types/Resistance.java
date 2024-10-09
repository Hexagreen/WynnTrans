package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Resistance extends WynnChatText {
    private final String resValue;
    private final String strValue;

    public Resistance(Text text, Pattern regex) {
        super(text, regex);
        this.resValue = matcher.group(1);
        this.strValue = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.resistance";
    }

    @Override
    protected void build() {
        MutableText buffs = Text.empty().append(Text.translatable(parentKey + ".res", resValue).setStyle(getStyle(2)));
        if(getSiblings().size() == 6) {
            buffs.append(Text.translatable(parentKey + ".and").setStyle(getStyle(3))).append(Text.translatable(parentKey + ".str", strValue).setStyle(getStyle(4)));
        }

        resultText = Text.empty();

        resultText.append(Text.translatable(parentKey, getSibling(0), buffs).setStyle(getStyle(1)));
    }
}
