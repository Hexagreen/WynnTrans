package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ObjectiveGlue extends TextGlue {
    protected ObjectiveGlue(Pattern regex, Class<? extends WynnChatText> wctClass) {
        super(regex, wctClass);
    }

    public static ObjectiveGlue get() {
        return new ObjectiveGlue(null, null);
    }

    @Override
    public boolean push(Text text) {
        return false;
    }
}
