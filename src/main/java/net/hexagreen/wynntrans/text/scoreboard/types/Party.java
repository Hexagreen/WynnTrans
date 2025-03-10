package net.hexagreen.wynntrans.text.scoreboard.types;

import net.hexagreen.wynntrans.text.scoreboard.WynnScoreboardText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class Party extends WynnScoreboardText {
    private static final Style style = Style.EMPTY.withColor(Formatting.YELLOW);

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 2) return false;
        return texts.getFirst().getString().matches("§e§lParty:§6 .+");
    }

    public Party(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "scoreboard.party";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        MutableText partyLevel = getSibling(0).getSiblings().getLast().copy();
        resultText.append(Text.translatable(translationKey, partyLevel.setStyle(partyLevel.getStyle().withBold(false)))
                .setStyle(style.withBold(true)));
        for(int i = 1, size = getSiblings().size(); i < size; i++) {
            resultText.append(getSibling(i));
        }
    }
}
