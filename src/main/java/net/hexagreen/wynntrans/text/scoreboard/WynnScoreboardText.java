package net.hexagreen.wynntrans.text.scoreboard;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.tooltip.types.SimpleTooltip;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class WynnScoreboardText extends WynnTransText {

    private static List<Text> colorCodedToStyledBatch(List<Text> textList) {
        return textList.parallelStream().map(WynnScoreboardText::colorCodedToStyled).toList();
    }

    public WynnScoreboardText(List<Text> texts) {
        super(siblingsToText(colorCodedToStyledBatch(texts)));
        resultText = Text.empty();
    }

    public List<Text> text() {
        try {
            build();
            return resultText.getSiblings();
        }
        catch(IndexOutOfBoundsException e) {
            WynnTrans.LOGGER.warn("IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OOB - Scoreboard");
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed scoreboard message.\n", e);
            return new SimpleTooltip(getSiblings()).text();
        }
        return getSiblings();
    }

    protected Text mergeTextStyleSide(List<Text> texts) {
        MutableText result = Text.empty();
        AtomicReference<StringBuilder> newBody = new AtomicReference<>(new StringBuilder());
        AtomicReference<Style> newStyle = new AtomicReference<>(texts.getFirst().getSiblings().getFirst().getStyle());
        for(Text text : texts) {
            int[] counter = {0};
            text.visit((style, string) -> {
                if(string.isEmpty()) return Optional.empty();
                if(counter[0]++ == 0) string = string.replaceFirst("^ ", "");
                if(!style.equals(newStyle.get())) {
                    result.append(Text.literal(newBody.get().toString()).setStyle(newStyle.get()));
                    newBody.set(new StringBuilder());
                    newStyle.set(style);
                }
                newBody.get().append(string);
                return Optional.empty();
            }, Style.EMPTY);
            String newBodyString = newBody.get().append(" ").toString();
            newBody.set(new StringBuilder(newBodyString.replaceAll(" {2}", " ")));
        }
        if(!newBody.get().isEmpty()) {
            String done = newBody.get().toString().replaceFirst(" +$", "").replaceAll(" {2}", " ");
            result.append(Text.literal(done).setStyle(newStyle.get()));
        }
        return result;
    }

    protected List<List<Text>> extractUniqueLines(List<Text> textsWithoutHead) {
        List<List<Text>> uniqueLines = new ArrayList<>();
        List<Text> uniqueLine = new ArrayList<>();
        for(Text line : textsWithoutHead) {
            if(line.getString().matches("^- .+")) {
                if(!uniqueLine.isEmpty()) uniqueLines.add(uniqueLine);
                uniqueLine = new ArrayList<>();
            }
            uniqueLine.add(line);
        }
        if(!uniqueLine.isEmpty()) uniqueLines.add(uniqueLine);

        return uniqueLines;
    }

    protected List<Text> wrapLine(Text text) {
        List<StringVisitable> svs = TEXT_HANDLER.wrapLines(text, 200, Style.EMPTY);
        List<Text> wrapped = new ArrayList<>();
        for(StringVisitable sv : svs) {
            MutableText tmp = Text.empty();
            sv.visit((style, string) -> {
                tmp.append(Text.literal(string).setStyle(style));
                return Optional.empty();
            }, Style.EMPTY);
            wrapped.add(tmp);
        }
        return wrapped;
    }
}
