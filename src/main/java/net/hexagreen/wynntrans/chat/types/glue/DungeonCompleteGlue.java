package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.DungeonComplete;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class DungeonCompleteGlue extends TextGlue {
    private static final Pattern REWARD = Pattern.compile("^§7\\[\\+.+]");
    private int count = 0;

    public DungeonCompleteGlue() {
        super(null, DungeonComplete.class);
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 0 && text.getString().contains("Great job!")) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        else if (count == 1 && REWARD.matcher(text.getString()).find()) {
            resetTimer();
            gluedText.append(text);
            safeNow();
            return true;
        }
        else {
            pop();
            return false;
        }
    }
}
