package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.LevelUp;
import net.hexagreen.wynntrans.text.chat.types.LevelUpProfession;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LevelUpGlue extends TextGlue {
    private static final Pattern PROFESSION = Pattern.compile("^§e +You are now level \\d+ in");
    private static final Pattern PROF_TAIL = Pattern.compile("^§5Only §d(\\d+) more levels? §5until you can");
    private static final Pattern REWARD = Pattern.compile("^§[7b]\\+");
    private int count = 0;
    private boolean isProfession = false;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§6 {32}§lLevel Up!$").matcher(text.getString()).find();
    }

    public LevelUpGlue() {
        super(LevelUp::new);
        gluedText.append(" ");
        count++;
    }

    @Override
    public boolean push(Text text) {
        if(text.getString().equals("\n")) return true;
        if(!isProfession) {
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
            this.changeWct(LevelUpProfession::new);
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
            else if(text.getString().isEmpty()) {
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
