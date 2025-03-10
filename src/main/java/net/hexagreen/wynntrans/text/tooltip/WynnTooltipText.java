package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.tooltip.types.SimpleTooltip;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class WynnTooltipText extends WynnTransText {

    protected static List<Text> colorCodedToStyledBatch(List<Text> textList) {
        return textList.parallelStream().map(WynnTooltipText::colorCodedToStyled).toList();
    }

    public WynnTooltipText(List<Text> texts) {
        super(siblingsToText(texts));
        resultText = Text.empty();
    }

    public List<Text> text() {
        try {
            build();
            return resultText.getSiblings();
        }
        catch(IndexOutOfBoundsException e) {
            WynnTrans.LOGGER.warn("IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OOB - Tooltip");
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed tooltip message.\n", e);
            return new SimpleTooltip(getSiblings()).text();
        }
        return getSiblings();
    }

    public MutableText textAsMutable() {
        try {
            build();
            return resultText;
        }
        catch(IndexOutOfBoundsException e) {
            WynnTrans.LOGGER.warn("IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OOB - Tooltip");
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed tooltip message.\n", e);
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
        AtomicReference<Style> newStyle = new AtomicReference<>(texts.getFirst().getSiblings().getFirst().getStyle());
        for(Text text : texts) {
            text.visit((style, string) -> {
                if(string.isEmpty()) return Optional.empty();
                if(!style.equals(newStyle.get())) {
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

    protected int getLongestWidth(List<Text> lines) {
        return getLongestWidth(lines, Text.literal(" "));
    }

    protected int getLongestWidth(List<Text> lines, Text criteria) {
        return getLongestWidth(lines, MinecraftClient.getInstance().textRenderer.getWidth(criteria));
    }

    protected int getLongestWidth(List<Text> lines, int criteria) {
        int max = criteria;
        for(Text line : lines) {
            max = Math.max(max, MinecraftClient.getInstance().textRenderer.getWidth(line));
        }
        return max;
    }
}
