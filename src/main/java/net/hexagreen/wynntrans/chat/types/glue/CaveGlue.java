package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.CaveCompleted;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

import java.util.regex.Pattern;

public class CaveGlue extends TextGlue {
    private static final Pattern REWARD = Pattern.compile("^ {11}- \\+.+$");
    private int count = 0;

    public CaveGlue() {
        super(null, CaveCompleted.class);
        gluedText.append("");
        count++;
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 1 && !text.getContent().equals(TextContent.EMPTY)) {
            pop();
            return false;
        }
        switch(count) {
            case 1, 2, 3, 4 -> {
                resetTimer();
                gluedText.append(text);
                count++;
                return true;
            }
            default -> {
                safeNow();
                if(REWARD.matcher(text.getString()).find()) {
                    resetTimer();
                    gluedText.append(text);
                    return true;
                }
                else {
                    pop();
                    return false;
                }
            }
        }
    }
}
