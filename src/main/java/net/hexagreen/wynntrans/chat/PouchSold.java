package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class PouchSold extends WynnChatText {

    public PouchSold(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "pouchSold";
    }

    @Override
    protected void build() {
        Style style = Style.EMPTY.withColor(Formatting.LIGHT_PURPLE);

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "_1").setStyle(style))
                .append(getSibling(1))
                .append(newTranslate(parentKey + "_2").setStyle(style))
                .append(getSibling(3))
                .append(newTranslate(parentKey + "_3").setStyle(style));
    }
}
