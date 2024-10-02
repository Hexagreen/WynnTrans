package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class TrinketOnActive extends WynnSystemText {
    private final Text item;
    private final Text time;

    public TrinketOnActive(Text text, Pattern regex) {
        super(text, regex);
        this.item = getSibling(0);
        this.time = ITime.translateTime(getContentString(2)).setStyle(getStyle(2));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.trinketOnActive";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().append(header);
        resultText.append(newTranslateWithSplit(parentKey, item, time).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
