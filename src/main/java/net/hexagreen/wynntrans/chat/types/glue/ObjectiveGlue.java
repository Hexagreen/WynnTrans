package net.hexagreen.wynntrans.chat.types.glue;

import net.hexagreen.wynntrans.chat.TextGlue;
import net.hexagreen.wynntrans.chat.types.ObjectiveComplete;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

import java.util.regex.Pattern;

public class ObjectiveGlue extends TextGlue {
    private static final Pattern FESTIVAL = Pattern.compile("^ +Festival of the .+$");
    private static final Pattern CLAIM = Pattern.compile("^ +Click here to claim your rewards!$");
    private static final Pattern REWARD = Pattern.compile("^ {5}- \\+.+$");
    private int count = 0;

    public ObjectiveGlue() {
        super(null, ObjectiveComplete.class);
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
            case 1, 2, 3, 5, 6 -> {
                resetTimer();
                gluedText.append(text);
                count++;
                return true;
            }
            default -> {
                safeNow();
                if(FESTIVAL.matcher(text.getString()).find()){
                    resetTimer();
                    gluedText.append(text);
                    count++;
                    return true;
                }
                if(CLAIM.matcher(text.getString()).find()) {
                    resetTimer();
                    gluedText.append(text);
                    pop();
                    return true;
                }
                if(REWARD.matcher(text.getString()).find()){
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
