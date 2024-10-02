package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.QuestCompleted;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class QuestGlue extends TextGlue {
    private static final Pattern REWARD = Pattern.compile("^§d {12,}- §.\\+.+$");
    private int count = 0;

    public QuestGlue() {
        super(null, QuestCompleted.class);
        gluedText.append(" ");
        count++;
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
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
