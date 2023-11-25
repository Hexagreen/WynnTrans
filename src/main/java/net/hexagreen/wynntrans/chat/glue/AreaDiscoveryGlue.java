package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.AreaDiscovery;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

public class AreaDiscoveryGlue extends TextGlue {
    private int count = 0;

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
                gluedText.append(text);
                pop();
                return true;
            }
            else {
                gluedText.append(text);
            }
        }
        else {
            if(text.getSiblings().size() == 2 && text.getSiblings().get(1).getStyle().getColor() == TextColor.fromFormatting(Formatting.GRAY)) {
                resetTimer();
                gluedText.append(text);
                return true;
            }
        }
        return false;
    }
}
