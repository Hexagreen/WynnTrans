package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.WorldEventFailed;
import net.minecraft.text.Text;

public class WorldEventFailedGlue extends TextGlue {
    private int count = 0;

    public WorldEventFailedGlue() {
        super(null, WorldEventFailed.class);
    }

    @Override
    public boolean push(Text text) {
        if(count == 0) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(count == 5) {
            resetTimer();
            if(text.getString().contains("Everyone in your group has died!")) {
                gluedText.append(text);
                count++;
                safeNow();
                return true;
            }
        }
        else if(count < 7) {
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
