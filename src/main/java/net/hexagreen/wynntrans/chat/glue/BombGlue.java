package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.BombStart;
import net.hexagreen.wynntrans.enums.ChatType;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombGlue extends TextGlue {
    private static final Pattern bombThanks = ChatType.BOMB_THANK.getRegex();

    protected BombGlue() {
        super(null, BombStart.class);
    }

    @Override
    public boolean push(Text text) {
        if(gluedText.equals(Text.empty())) {
            gluedText = text.copy();
            return true;
        }
        if(!bombThanks.matcher(text.getString()).find()) {
            return true;
        }
        else {
            safeNow();
            pop();
            return false;
        }
    }
}
