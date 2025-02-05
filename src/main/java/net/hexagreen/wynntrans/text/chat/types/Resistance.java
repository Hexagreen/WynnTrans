package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Resistance extends WynnChatText {
    private final String resValue;
    private final String strValue;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^.+ has given you (\\d+%) resistance(?: and (\\d+%) strength)?").matcher(text.getString()).find();
    }

    public Resistance(Text text) {
        super(text);
        this.resValue = inputText.getString().replaceFirst(".+you (\\d+%) resistance.+", "$1");
        this.strValue = inputText.getString().replaceFirst(".+and (\\d+%) strength\\.", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.resistance";
    }

    @Override
    protected void build() {
        MutableText buffs = Text.empty().append(Text.translatable(translationKey + ".res", resValue).setStyle(getStyle(2)));
        if(getSiblings().size() == 6) {
            buffs.append(Text.translatable(translationKey + ".and").setStyle(getStyle(3))).append(Text.translatable(translationKey + ".str", strValue).setStyle(getStyle(4)));
        }

        resultText = Text.empty();

        resultText.append(Text.translatable(translationKey, getSibling(0), buffs).setStyle(getStyle(1)));
    }
}
