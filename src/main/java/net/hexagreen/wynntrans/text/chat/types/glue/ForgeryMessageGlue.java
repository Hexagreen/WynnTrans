package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.ForgeryMessage;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ForgeryMessageGlue extends TextGlue {
    private static final Pattern KILL_REQUIRE = Pattern.compile("^§7You must kill §b\\d+ §7mobs and complete");
    private static final Pattern KILL_FULFILL = Pattern.compile("^§7Complete the dungeon for this run to count");
    private static final Pattern RUN_COUNT = Pattern.compile("^§b\\[\\d/\\d] §7Dungeon Runs complete\\.");
    private static final Pattern TIME_REMAIN = Pattern.compile("remaining\\.$");
    private static final Pattern REWARD_CLAIM = Pattern.compile("^§bRight-Click §7again to claim §7your reward");
    private static final Pattern COMPLETE = Pattern.compile("^§7You have completed the challenge");
    private int count = 0;
    private String context;

    public static boolean typeChecker(Text text) {
        return text.getString().matches("^§6§lForgery Chest( Claimed!)?");
    }

    public ForgeryMessageGlue() {
        super(ForgeryMessage::new);
    }

    @Override
    public boolean push(Text text) {
        String _str = text.getString();
        switch(count) {
            case 0 -> {
                appendAndInc(text);
                return true;
            }
            case 1 -> {
                if(KILL_REQUIRE.matcher(_str).find()) {
                    appendAndInc(text);
                    safeNow();
                    pop("KILL_REQUIRE");
                    return true;
                }
                else if(KILL_FULFILL.matcher(_str).find()) {
                    appendAndInc(text);
                    safeNow();
                    pop("KILL_FULFILL");
                    return true;
                }
                else if(TIME_REMAIN.matcher(_str).find()) {
                    appendAndInc(text);
                    safeNow();
                    pop("TIME_REMAIN");
                    return true;
                }
                else if(RUN_COUNT.matcher(_str).find()) {
                    appendAndInc(text);
                    context = "RUN_COUNT";
                    return true;
                }
                else if(REWARD_CLAIM.matcher(_str).find()) {
                    appendAndInc(text);
                    context = "REWARD_CLAIM";
                    return true;
                }
                else if(COMPLETE.matcher(_str).find()) {
                    appendAndInc(text);
                    return true;
                }
            }
            case 2 -> {
                if(_str.replaceFirst("§.", "").isBlank()) {
                    appendAndInc(text);
                    return true;
                }
            }
            case 3 -> {
                switch(_str) {
                    case "§bTimer enabled" -> {
                        resetTimer();
                        gluedText.append(text);
                        count = 10;
                        return true;
                    }
                    case "§3            Time remaining" -> {
                        resetTimer();
                        gluedText.append(text);
                        count = 20;
                        return true;
                    }
                    case "§3            Complete Dungeons:" -> {
                        resetTimer();
                        gluedText.append(text);
                        count = 30;
                        return true;
                    }
                    case "§d    Rewards:" -> {
                        resetTimer();
                        gluedText.append(text);
                        count = 40;
                        return true;
                    }
                }
            }
            case 10 -> {
                if(_str.equals("§b- §73 hours remaining")) {
                    resetTimer();
                    gluedText.append(text);
                    safeNow();
                    pop("RUN_COUNT");
                    return true;
                }
            }
            case 20 -> {
                if(_str.matches("^§d            - .+ remaining\\.$")) {
                    resetTimer();
                    gluedText.append(text);
                    safeNow();
                    pop(context);
                    return true;
                }
            }
            case 30 -> {
                if(_str.matches("§d            - §fCorrupted .+")) {
                    resetTimer();
                    gluedText.append(text);
                    return true;
                }
                if(_str.replaceFirst("§.", "").isBlank()) {
                    resetTimer();
                    gluedText.append(text);
                    count = 3;
                    return true;
                }
            }
            case 40 -> {
                if(_str.matches("^§d    \\+ §f\\d+ §7Combat XP$")) {
                    resetTimer();
                    gluedText.append(text);
                    return true;
                }
                if(_str.equals("§d    + §fVarious Items")) {
                    resetTimer();
                    gluedText.append(text);
                    safeNow();
                    return true;
                }
                if(_str.matches("^§d    \\+ .+")) {
                    resetTimer();
                    gluedText.append(text);
                    return true;
                }
                pop("COMPLETE");
                return false;
            }
        }
        pop();
        return false;
    }

    private void appendAndInc(Text text) {
        resetTimer();
        gluedText.append(text);
        count++;
    }
}
