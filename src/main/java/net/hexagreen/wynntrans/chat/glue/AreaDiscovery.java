package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;
//TODO: 전체구현
public class AreaDiscovery extends TextGlue {

    protected AreaDiscovery(Pattern regex, Class<? extends WynnChatText> wctClass) {
        super(regex, wctClass);
    }

    @Override
    public boolean push(Text text) {
        return false;
    }
}
