package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.LevelUp;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

import java.util.regex.Pattern;

public class LevelUpGlue extends TextGlue {
    private static final Pattern REWARD = Pattern.compile("^\\+");
    private int count = 0;

    protected LevelUpGlue(Pattern regex, Class<? extends WynnChatText> wctClass) {
        super(regex, wctClass);
        gluedText.append("");
        count++;
    }

    public static LevelUpGlue get() {
        return new LevelUpGlue(null, LevelUp.class);
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(count == 1 && !text.getContent().equals(TextContent.EMPTY)) {
            pop();
            return false;
        }
        switch(count) {
            case 1, 2, 3, 4, 5 -> {
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
