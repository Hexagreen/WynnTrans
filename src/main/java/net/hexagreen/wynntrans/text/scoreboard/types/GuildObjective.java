package net.hexagreen.wynntrans.text.scoreboard.types;

import net.hexagreen.wynntrans.enums.Objectives;
import net.hexagreen.wynntrans.text.scoreboard.WynnScoreboardText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class GuildObjective extends WynnScoreboardText {
    private static final Style style = Style.EMPTY.withColor(Formatting.AQUA);

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() <= 1) return false;
        return texts.getFirst().getString().matches("§b§lGuild Obj: .+");
    }

    public GuildObjective(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "scoreboard.guildObjective";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> texts = new ArrayList<>(getSiblings());
        String counter = texts.removeFirst().getString().replaceFirst(".+: ", "");
        Text head = Text.translatable(translationKey, counter).setStyle(style.withBold(true));
        resultText.append(head);

        boolean completed = false;
        Text command = Text.empty();
        if(getSiblings().size() > 3) {
            command = texts.removeLast();
            texts.removeLast();
            texts.removeLast();
            completed = true;
        }

        List<Text> lines = extractUniqueLines(texts).stream()
                .map(this::mergeTextStyleSide)
                .toList();

        List<Text> siblings = lines.getFirst().getSiblings();
        Text nowCount = siblings.get(siblings.size() - 2);
        Text needCount = siblings.getLast();
        String objectiveNameString = siblings.get(1).getString().replaceAll(" {2,}", " ").replaceFirst(": ", "");
        Text objective = Objectives.findNormalized(objectiveNameString).getTranslatedText();

        MutableText line = Text.empty().setStyle(GRAY)
                .append(siblings.getFirst()).append(objective).append(": ").append(nowCount).append(needCount);
        if(completed) line.append("\n \n")
                .append(Text.translatable(translationKey + ".complete", command).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
        wrapLine(line).forEach(resultText::append);
    }
}
