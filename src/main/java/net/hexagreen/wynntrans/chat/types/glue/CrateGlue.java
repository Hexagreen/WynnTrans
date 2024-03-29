package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.CrateGetPersonal;
import net.minecraft.text.Text;

public class CrateGlue extends TextGlue {
    private int count = 0;

    public CrateGlue() {
        super(null, CrateGetPersonal.class);
        gluedText.append("");
        count++;
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
