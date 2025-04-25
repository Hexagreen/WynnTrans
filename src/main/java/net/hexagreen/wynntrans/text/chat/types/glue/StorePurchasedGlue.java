package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.StorePurchased;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class StorePurchasedGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("§6§l +Thank you for your purchase!").matcher(text.getString()).find();
    }

    public StorePurchasedGlue() {
        super(StorePurchased::new);
    }

    @Override
    public boolean push(Text text) {
        if(count == 0) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        if(count == 1) {
            resetTimer();
            safeNow();
            pop();
            return true;
        }
        return false;
    }
}
