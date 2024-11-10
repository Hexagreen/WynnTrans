package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.CaveCompleted;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CaveGlue extends TextGlue {
    private static final Pattern REWARD = Pattern.compile("^ {11}- \\+.+$");
    private int count = 0;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^ +\\[Cave Completed]$").matcher(text.getString()).find();
    }

    public CaveGlue() {
        super(CaveCompleted::new);
        gluedText.append("");
        count++;
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 1 && !text.getContent().equals(PlainTextContent.EMPTY)) {
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
