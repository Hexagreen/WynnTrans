package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.LootrunMissionComplete;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LootrunMissionCompleteGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 2) return false;
        return text.getSiblings().get(1).getString().matches("§b§lMission Completed");
    }

    public LootrunMissionCompleteGlue() {
        super(LootrunMissionComplete::new);
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
                if(text.getString().matches("§7.§7§e.+")) {
                    resetTimer();
                    gluedText.append(text);
                    count--;
                    return true;
                }
            }
            case 2 -> {
                if(Identifier.of("minecraft:space").equals(text.getSiblings().getFirst().getStyle().getFont())) {
                    resetTimer();
                    gluedText.append(text);
                    if(text.getString().matches("§7\uDB00\uDC9A")) {
                        safeNow();
                        pop();
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
