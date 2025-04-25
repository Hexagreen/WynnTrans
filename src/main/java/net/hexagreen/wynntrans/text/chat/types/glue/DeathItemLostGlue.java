package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.DeathItemLost;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DeathItemLostGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§6By dying, you've lost:").matcher(text.getString()).find();
    }

    public DeathItemLostGlue() {
        super(DeathItemLost::new);
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 0 && text.getString().contains("you've lost:")) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(text.getString().matches("^§6- .+$")) {
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