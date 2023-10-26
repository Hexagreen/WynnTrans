package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CaveGlue extends TextGlue {
    protected CaveGlue(Pattern regex, Class<? extends WynnChatText> wctClass) {
        super(regex, wctClass);
    }

    public static CaveGlue get() {
        return new CaveGlue(null, null);
    }

    @Override
    public boolean push(Text text) {
        return false;
    }
}
