package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class TrinketRunout extends WynnSystemText {
    private final Text item;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("The effects of the .+ have worn off.").matcher(removeTextBox(text)).find();
    }

    public TrinketRunout(Text text) {
        super(text);
        this.item = getSibling(1);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.trinketRunout";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(Text.translatable(translationKey, item).setStyle(getStyle(0)));
    }
}
