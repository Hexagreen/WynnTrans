package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.LootrunChallengeFail;
import net.minecraft.text.Text;

public class LootrunChallengeFailGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 2) return false;
        return text.getSiblings().get(1).getString().matches("§c§lChallenge Failed!");
    }

    public LootrunChallengeFailGlue() {
        super(LootrunChallengeFail::new);
    }

    @Override
    public boolean push(Text text) {
        if(count == 3 && text.getString().isEmpty()) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        switch(count) {
            case 0, 1, 2 -> {
                resetTimer();
                gluedText.append(text);
                count++;
                return true;
            }
            case 4 -> {
                if(text.getString().matches("§7.§7\\[\\+\\d+%.+") || text.getString().isEmpty()) {
                    resetTimer();
                    gluedText.append(text);
                    safeNow();
                    return true;
                }
            }
        }
        return false;
    }
}
