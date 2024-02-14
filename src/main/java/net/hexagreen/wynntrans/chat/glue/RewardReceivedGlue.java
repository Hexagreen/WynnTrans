package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.RewardReceived;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RewardReceivedGlue extends TextGlue {
    private static final Pattern TAIL = Pattern.compile("^Visit wynncraft\\.com/store to get your own!");
    private int count = 0;

    public RewardReceivedGlue() {
        super(null, RewardReceived.class);
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 0 && text.getString().contains("You have received:")) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if(text.getString().matches("^- .+$")) {
            resetTimer();
            gluedText.append(text);
            return true;
        }
        else if(TAIL.matcher(text.getString()).find()) {
            resetTimer();
            safeNow();
            gluedText.append(text);
            pop();
            return true;
        }
        else {
            pop();
            return false;
        }
    }
}