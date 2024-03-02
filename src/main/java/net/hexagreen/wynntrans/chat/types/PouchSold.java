package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
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
        resultText.append(newTranslate(parentKey, getSibling(1), getSibling(3)).setStyle(style));
    }
}
