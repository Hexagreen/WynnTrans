package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.List;

public class ContentBookFilterAndSort extends WynnTooltipText {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().equals("Filter") || texts.getFirst().getString().equals("Sort");
    }

    public ContentBookFilterAndSort(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        if(getSibling(0).getString().equals("Sort")) {
            resultText = new SimpleTooltip(getSiblings().subList(0, getSiblings().size() - 3)).textAsMutable();
        }
        else {
            resultText = new SimpleTooltip(getSiblings().subList(0, 2)).textAsMutable();
            for(Text line : getSiblings().subList(2, getSiblings().size() - 3)) {
                String v = line.getSiblings().get(1).getString();
                String k = "wytr.content." + normalizeStringForKey(v);
                WTS.checkTranslationExist(k, v);
                Text contentName = Text.translatableWithFallback(k, v).setStyle(line.getSiblings().get(1).getStyle());

                MutableText newLine = line.copyContentOnly().setStyle(line.getStyle())
                        .append(line.getSiblings().getFirst())
                        .append(contentName);

                resultText.append(newLine);
            }
        }
        resultText.append(" ")
                .append(Text.translatable("wytr.tooltip.nextPage"))
                .append(Text.translatable("wytr.tooltip.previousPage"));
    }
}
