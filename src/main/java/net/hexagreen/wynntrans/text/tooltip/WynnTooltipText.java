package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.tooltip.types.SimpleTooltip;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class WynnTooltipText extends WynnTransText {

    protected static List<Text> colorCodedToStyledBatch(List<Text> textList) {
        return textList.stream().map(WynnTooltipText::colorCodedToStyled).toList();
    }

    protected static List<Text> textToList(Text text) {
        List<Text> wrapped = new ArrayList<>();
        wrapped.add(text);
        return wrapped;
    }

    public WynnTooltipText(List<Text> texts) {
        super(siblingsToText(colorCodedToStyledBatch(texts)));
        resultText = Text.empty();
    }

    public WynnTooltipText(List<Text> texts, boolean redirectStyling) {
        super(redirectStyling ? siblingsToText(texts) : siblingsToText(colorCodedToStyledBatch(texts)));
        resultText = Text.empty();
    }

    public List<Text> text() {
        try {
            build();
            return resultText.getSiblings();
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed tooltip message.\n", e);
            return new SimpleTooltip(getSiblings()).text();
        }
        catch(Exception e) {
            WynnTrans.LOGGER.warn("Exception occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "TooltipExc");
        }
        return getSiblings();
    }

    public MutableText textAsMutable() {
        try {
            build();
            return resultText;
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed tooltip message.\n", e);
        }
        catch(Exception e) {
            WynnTrans.LOGGER.warn("Exception occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "TooltipExc");
        }
        return inputText;
    }

    protected Text mergeTextStyleSide(Text text) {
        List<Text> texts = new ArrayList<>();
        texts.add(text);
        return mergeTextStyleSide(texts);
    }

    protected Text mergeTextStyleSide(List<Text> texts) {
        MutableText result = Text.empty();
        AtomicReference<StringBuilder> newBody = new AtomicReference<>(new StringBuilder());
        AtomicReference<Style> newStyle = new AtomicReference<>(Style.EMPTY);
        for(Text text : texts) {
            text.visit((style, string) -> {
                if(string.isEmpty()) return Optional.empty();
                if(!style.equals(newStyle.get())) {
                    if(!newBody.get().isEmpty())
                        result.append(Text.literal(newBody.get().toString()).setStyle(newStyle.get()));
                    newBody.set(new StringBuilder());
                    newStyle.set(style);
                }
                newBody.get().append(string);
                return Optional.empty();
            }, Style.EMPTY);
            newBody.get().append(" ");
        }
        if(!newBody.get().isEmpty()) {
            String done = newBody.get().toString().replaceFirst(" +$", "");
            result.append(Text.literal(done).setStyle(newStyle.get()));
        }
        return result;
    }

    protected int getLongestWidth(Collection<Text> lines) {
        return getLongestWidth(lines, Text.literal(" "));
    }

    protected int getLongestWidth(Collection<Text> lines, Text criteria) {
        return getLongestWidth(lines, TEXT_RENDERER.getWidth(criteria));
    }

    protected int getLongestWidth(Collection<Text> lines, int criteria) {
        int max = criteria;
        for(Text line : lines) {
            max = Math.max(max, TEXT_RENDERER.getWidth(line));
        }
        return max;
    }
}
