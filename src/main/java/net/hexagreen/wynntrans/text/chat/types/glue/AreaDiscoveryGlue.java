package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.AreaDiscovery;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class AreaDiscoveryGlue extends TextGlue {
    private int count = 0;
    private boolean shortForm = false;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^ *§[67]Area Discovered: §[ef]").matcher(text.getString()).find();
    }

    public AreaDiscoveryGlue() {
        super(AreaDiscovery::new);
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
            if(text.getString().isEmpty()) {
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
            else if(text.getString().isBlank()) {
                resetTimer();
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
