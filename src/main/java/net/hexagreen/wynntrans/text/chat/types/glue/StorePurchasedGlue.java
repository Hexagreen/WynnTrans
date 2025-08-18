package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.StorePurchased;
import net.minecraft.text.Text;

public class StorePurchasedGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return text.getString()
                .matches("^\uDAFF\uDFFC(\uE014\uDAFF\uDFFF\uE002\uDAFF\uDFFE|\uE001\uDB00\uDC06) §7\uDB00\uDC36Thank you for your purchase!$");
    }

    public StorePurchasedGlue() {
        super(StorePurchased::new);
    }

    @Override
    public boolean push(Text text) {
        String str = text.getString();
        if(count == 0) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(count == 1 && str.contains("Your continuous support")) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(count == 2) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(count == 3 && str.contains("You have received")) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(count == 4 && str.matches("\uDAFF\uDFFC\uE001\uDB00\uDC06 - .+")) {
            resetTimer();
            gluedText.append(text);
            safeNow();
            return true;
        }
        else if(count == 4 && str.matches("\uDAFF\uDFFC\uE001\uDB00\uDC06 ")) {
            resetTimer();
            gluedText.append(text);
            safeNow();
            pop();
            return true;
        }
        return false;
    }
}
