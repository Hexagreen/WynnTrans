package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.CrateGetPersonal;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CrateGlue extends TextGlue {
    private int count = 0;

    protected CrateGlue(Pattern regex, Class<? extends WynnChatText> wctClass) {
        super(regex, wctClass);
        gluedText.append("");
        count++;
    }

    public static CrateGlue get() {
        return new CrateGlue(null, CrateGetPersonal.class);
    }

    @Override
    public boolean push(Text text) {
        if(count <= 4) {
            resetTimer();
            gluedText.append(text);
            count++;
        }
        else {
            gluedText.append("");
            safeNow();
            pop();
        }
        return true;
    }
}
