package net.hexagreen.wynntrans.text.scoreboard.types;

import net.hexagreen.wynntrans.enums.Objectives;
import net.hexagreen.wynntrans.text.scoreboard.WynnScoreboardText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class DailyObjective extends WynnScoreboardText {
    private static final Style style = Style.EMPTY.withColor(Formatting.RED);

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() <= 1) return false;
        return texts.getFirst().getString().equals("§c§lDaily Objective:");
    }

    public DailyObjective(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "scoreboard.dailyObjective";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> texts = new ArrayList<>(getSiblings());
        texts.removeFirst();
        Text head = Text.translatable(translationKey).setStyle(style.withBold(true));
        resultText.append(head);

        List<Text> lines = extractUniqueLines(texts).stream()
                .map(this::mergeTextStyleSide)
                .toList();

        if(lines.getFirst().getString().contains("All done")) {
            Text done = Text.literal("- ").setStyle(style)
                    .append(Text.translatable("wytr.scoreboard.objectiveDone").setStyle(GRAY));
            resultText.append(done);
            return;
        }

        List<Text> siblings = lines.getFirst().getSiblings();
        Text nowCount = siblings.get(siblings.size() - 2);
        Text needCount = siblings.getLast();
        String objectiveNameString = siblings.get(1).getString().replaceAll(" {2,}", " ").replaceFirst(": ", "");
        Text objective = Objectives.findNormalized(objectiveNameString).getTranslatedText();

        Text line = Text.empty().setStyle(GRAY)
                .append(siblings.getFirst()).append(objective).append(": ").append(nowCount).append(needCount);
        wrapLine(line).forEach(resultText::append);
    }
}
