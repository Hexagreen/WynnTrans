package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class WorldEventStart extends WynnSystemText {

    public WorldEventStart(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, UnprocessedChatTypeException {

    }
}
