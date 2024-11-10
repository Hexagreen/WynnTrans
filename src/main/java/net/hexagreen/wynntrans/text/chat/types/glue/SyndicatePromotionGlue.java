package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.SyndicatePromotion;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class SyndicatePromotionGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("§3✮§b§l Silverbull Trading Company §3✮").matcher(text.getString()).find();
    }

    public SyndicatePromotionGlue() {
        super(SyndicatePromotion::new);
    }

    @Override
    public boolean push(Text text) {
        if(count == 0) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(text.getString().contains("You have received a promotion")
                || text.getString().contains("Division") || count == 4) {
            resetTimer();
            gluedText.append(text);
            count++;
            safeNow();
            return true;
        }
        else if(text.getString().isEmpty() && count == 3) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(count >= 5 && (text.getString().isEmpty() || text.getString().matches("(?s).+\\[.+]"))) {
            resetTimer();
            gluedText.append(text);
            count++;
            safeNow();
            return true;
        }
        else {
            pop();
            return false;
        }
    }
}
