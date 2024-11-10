package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.WorldEventComplete;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class WorldEventCompleteGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("\uDAFF\uDFBE\uE016\uE00E\uE011\uE00B\uE003 \uE004\uE015\uE004\uE00D\uE013\uDB00\uDC02$").matcher(text.getString()).find();
    }

    public WorldEventCompleteGlue() {
        super(WorldEventComplete::new);
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
