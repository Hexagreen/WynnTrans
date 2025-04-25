package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.CrateGetPersonal;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CrateGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§.§.You've gotten a .+§.§. reward!").matcher(text.getString()).find();
    }

    public CrateGlue() {
        super(CrateGetPersonal::new);
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
