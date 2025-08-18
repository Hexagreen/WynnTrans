package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.enums.Bombs;
import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.BombStart;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombStartGlue extends TextGlue {

    public static final Pattern PREFIX = Pattern.compile("\uDAFF\uDFFC\uE001\uDB00\uDC06|\uDAFF\uDFFC\uE014\uDAFF\uDFFF\uE002\uDAFF\uDFFE");
    int count = 0;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() < 4) return false;
        if(!PREFIX.matcher(text.getString()).find()) return false;
        String name = text.getSiblings().getLast().getString();
        return name.matches(".+ Bomb") && Bombs.findBomb(name) != null;
    }

    public BombStartGlue() {
        super(BombStart::new);
    }

    @Override
    public boolean push(Text text) {
        if(count == 0) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(count < 5) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(text.getString().contains("Thank ")) {
            resetTimer();
            gluedText.append(text);
            count++;
            safeNow();
            return true;
        }
        else {
            pop();
            return true;
        }
    }
}
