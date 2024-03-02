package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.DeathItemLost;
import net.minecraft.text.Text;

public class DeathItemLostGlue extends TextGlue {
    private int count = 0;

    public DeathItemLostGlue() {
        super(null, DeathItemLost.class);
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 0 && text.getString().contains("You've lost:")) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(text.getString().matches("^- .+$")) {
            safeNow();
            resetTimer();
            gluedText.append(text);
            return true;
        }
        else {
            pop();
            return false;
        }
    }
}