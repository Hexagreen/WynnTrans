package net.hexagreen.wynntrans.chat.glue;

import net.hexagreen.wynntrans.chat.LevelUp;
import net.hexagreen.wynntrans.chat.LevelUpProfession;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;
import net.minecraft.text.TextContent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelUpGlue extends TextGlue {
    private static final Pattern PROFESSION = Pattern.compile("^ +You are now level \\d+ in");
    private static final Pattern PROF_TAIL = Pattern.compile("^Only (\\d+) more levels until you can");
    private static final Pattern REWARD = Pattern.compile("^\\+");
    private int count = 0;
    private boolean isProfession = false;

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
        if (!isProfession) {
            switch(count) {
                case 1, 3, 4, 5 -> {
                    resetTimer();
                    gluedText.append(text);
                    count++;
                    return true;
                }
                case 2 -> {
                    Matcher prof = PROFESSION.matcher(text.getString());
                    if(prof.find()) this.isProfession = true;
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
        else {
            this.changeWct(LevelUpProfession.class);
            if(count == 3) {
                resetTimer();
                gluedText.append(text);
                count++;
                return true;
            }

            safeNow();
            if(REWARD.matcher(text.getString()).find()) {
                resetTimer();
                gluedText.append(text);
                return true;
            }
            else if(text.getString().equals("")) {
                resetTimer();
                gluedText.append(text);
                return true;
            }
            else if(PROF_TAIL.matcher(text.getString()).find()) {
                resetTimer();
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
}
