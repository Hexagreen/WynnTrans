package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.WorldEventComplete;
import net.minecraft.text.Text;

public class WorldEventCompleteGlue extends TextGlue {
    private int count = 0;

    public WorldEventCompleteGlue() {
        super(null, WorldEventComplete.class);
    }

    @Override
    public boolean push(Text text) {
        if(count == 0) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(count == 4) {
            resetTimer();
            if(text.getString().contains("Claim your rewards at the chest!")) {
                gluedText.append(text);
                count++;
                safeNow();
                return true;
            }
        }
        else if(count < 6) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else {
            pop();
            return true;
        }
        return false;
    }
}
