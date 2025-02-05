package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.LootrunChallengeComplete;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LootrunChallengeCompleteGlue extends TextGlue {
    private int count = 0;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 2) return false;
        return text.getSiblings().get(1).getString().matches("§a§lChallenge Completed");
    }

    public LootrunChallengeCompleteGlue() {
        super(LootrunChallengeComplete::new);
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
                safeNow();
                return true;
            }
            case 3 -> {
                if(text.getString().matches("§7.§7\\[\\+\\d+%.+")
                        || text.contains(Text.literal("§5§lCurses"))
                        || text.contains(Text.literal("§b§lMission Started"))
                        || text.contains(Text.literal("§6§lBoon"))
                        || text.getString().contains("§b§l")) {
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
                    return true;
                }
            }
        }
        return false;
    }
}
