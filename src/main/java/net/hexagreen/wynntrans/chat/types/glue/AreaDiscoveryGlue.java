package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.AreaDiscovery;
import net.minecraft.text.Text;

public class AreaDiscoveryGlue extends TextGlue {
    private int count = 0;
    private boolean shortForm = false;

    public AreaDiscoveryGlue() {
        super(null, AreaDiscovery.class);
        gluedText.append(" ");
        count++;
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 1) {
            resetTimer();
            count++;
            if(text.getString().matches("^ *§7.+")) {
                safeNow();
                gluedText.append(text);
                shortForm = true;
            }
            else {
                gluedText.append(text);
            }
            return true;
        }
        else if(count == 2) {
            if(text.getString().equals(" ")) {
                resetTimer();
                count++;
                if(shortForm) pop();
                return true;
            }
        }
        else {
            if(text.getString().matches("^ +§.+")) {
                resetTimer();
                safeNow();
                gluedText.append(text);
                return true;
            }
            else {
                pop();
                return false;
            }
        }
        return false;
    }
}
