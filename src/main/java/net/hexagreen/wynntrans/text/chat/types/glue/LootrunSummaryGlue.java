package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.LootrunSummary;
import net.minecraft.text.Text;

public class LootrunSummaryGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 2) return false;
        return text.getSiblings().get(1).getString().matches("§6§lLootrun Completed!");
    }

    public LootrunSummaryGlue() {
        super(LootrunSummary::new);
    }

    @Override
    public boolean push(Text text) {
        if(count == 2 && text.getString().isEmpty()) {
            resetTimer();
            gluedText.append(text);
            count++;
            return true;
        }
        switch(count) {
            case 0, 1 -> {
                resetTimer();
                gluedText.append(text);
                count++;
                return true;
            }
            case 3 -> {
                if(text.getString().contains("Reward")) {
                    resetTimer();
                    gluedText.append(text);
                    return true;
                }
                else if(text.getString().contains("§7 Lootrun Experience")) {
                    resetTimer();
                    gluedText.append(text);
                    safeNow();
                    pop();
                    return true;
                }
            }
        }
        return false;
    }
}
