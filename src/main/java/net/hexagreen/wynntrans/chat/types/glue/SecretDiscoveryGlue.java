package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.SecretDiscovery;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecretDiscoveryGlue extends TextGlue {
    private static final Pattern DISCOVERY_AREA = Pattern.compile(" +.+ \\[\\d+/\\d+]");
    private int count = 0;

    public SecretDiscoveryGlue(){
        super(null, SecretDiscovery.class);
        gluedText.append("");
    }
    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 0 && !text.getString().startsWith(" ")) {
            MutableText reform = Text.empty().append(Text.empty());
            for(Text sibling : text.getSiblings()) {
                reform.append(sibling);
            }
            text = reform;
        }
        if(count == 0) {
            resetTimer();
            count++;
            gluedText.append(text);
            return true;
        }
        else if(count == 1) {
            Matcher m = DISCOVERY_AREA.matcher(text.getString());
            if(m.find()) {
                resetTimer();
                count++;
                gluedText.append(text);
                return true;
            }
        }
        else if(count == 2) {
            if(text.getString().equals(" ")) {
                resetTimer();
                count++;
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
        }
        return false;
    }
}
