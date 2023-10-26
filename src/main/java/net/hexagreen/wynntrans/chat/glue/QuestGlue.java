package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.QuestCompleted;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

import java.util.regex.Pattern;

public class QuestGlue extends TextGlue {
    private static final Pattern REWARD = Pattern.compile("^            - \\+.+$");
    protected QuestGlue(Pattern regex, Class<? extends WynnChatText> wctClass) {
        super(regex, wctClass);
        gluedText.append("");
    }

    public static QuestGlue get() {
        return new QuestGlue(null, QuestCompleted.class);
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        int size = gluedText.getSiblings().size();
        if(size == 1 && !text.getContent().equals(TextContent.EMPTY)) {
            pop();
            return false;
        }
        switch(size) {
            case 1, 2, 3, 4 -> {
                resetTimer();
                gluedText.append(text);
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
