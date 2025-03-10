package net.hexagreen.wynntrans.text.scoreboard.types;

import net.hexagreen.wynntrans.enums.Objectives;
import net.hexagreen.wynntrans.text.scoreboard.WynnScoreboardText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class StarterObjective extends WynnScoreboardText {
    private static final Style style = Style.EMPTY.withColor(Formatting.GREEN);

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() <= 1) return false;
        return texts.getFirst().getString().equals("§a§lObjectives:");
    }

    public StarterObjective(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "scoreboard.starterObjective";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> texts = new ArrayList<>(getSiblings());
        texts.removeFirst();
        Text head = Text.translatable(translationKey).setStyle(style.withBold(true));
        resultText.append(head);

        List<Text> merged = extractUniqueLines(texts).stream()
                .map(this::mergeTextStyleSide)
                .toList();

        if(merged.getFirst().getString().matches("^- .+: \\d+/\\d+")) {
            throw new TextTranslationFailException("StarterObjective");
        }

        List<Text> lines = new ArrayList<>();
        for(Text l : merged) {
            List<Text> siblings = l.getSiblings();
            Text nowCount = siblings.get(siblings.size() - 2);
            Text needCount = siblings.getLast();
            String objectiveNameString = siblings.get(1).getString().replaceAll(" {2,}", " ").replaceFirst(": ", "");
            Text objective = Objectives.findNormalized(objectiveNameString).getTranslatedText();

            Text line = Text.empty().setStyle(GRAY)
                    .append(siblings.getFirst()).append(objective).append(": ").append(nowCount).append(needCount);
            lines.add(line);
        }

        lines.forEach(resultText::append);
    }
}
