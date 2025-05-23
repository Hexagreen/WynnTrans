package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.WorldEventFailed;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class WorldEventFailedGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(?:\uDAFF\uDFFC\uE00D\uDAFF\uDFFF\uE002\uDAFF\uDFFE|\uDAFF\uDFFC\uE001\uDB00\uDC06) You have failed ").matcher(text.getString()).find();
    }

    public WorldEventFailedGlue() {
        super(WorldEventFailed::new);
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
            if(text.getString().contains("Everyone in your group has died!") || text.getString().contains("The time limit has expired!")) {
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
