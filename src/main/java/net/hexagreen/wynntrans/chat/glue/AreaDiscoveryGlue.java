package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.AreaDiscovery;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class AreaDiscoveryGlue extends TextGlue {
    private int count = 0;
    private boolean shortForm = false;

    public AreaDiscoveryGlue() {
        super(null, AreaDiscovery.class);
        gluedText.append("");
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 0) {
            resetTimer();
            count++;
            if(text.getSiblings().get(2).getStyle().getColor() == TextColor.fromFormatting(Formatting.WHITE)) {
                safeNow();
                gluedText.append(text);
                shortForm = true;
            }
            else {
                gluedText.append(text);
            }
            return true;
        }
        else if(count == 1) {
            if(text.getString().equals(" ")) {
                resetTimer();
                count++;
                if(shortForm) pop();
                return true;
            }
        }
        else {
            if(text.getSiblings().size() == 2 && text.getSiblings().get(1).getStyle().getColor() == TextColor.fromFormatting(Formatting.GRAY)) {
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
