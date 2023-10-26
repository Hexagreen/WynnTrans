package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class LevelUpGlue extends TextGlue {
    protected LevelUpGlue(Pattern regex, Class<? extends WynnChatText> wctClass) {
        super(regex, wctClass);
    }

    public static LevelUpGlue get() {
        return new LevelUpGlue(null, null);
    }

    @Override
    public boolean push(Text text) {
        return false;
    }
}
