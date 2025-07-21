package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Rune extends WynnTooltipText implements ITradableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("§.(Az|Nii|Uth|Tol|Ek) RuneÀ*");
    }

    public Rune(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        insertPriceSection(segments, resultText::append, (seg, i, lastIndex) -> {
            if(i == 0 || seg.getFirst().getString().equals("Use this item to enter Raids")) translateNameSection(seg);
            else seg.forEach(resultText::append);
            if(i != lastIndex) resultText.append(SPLITTER);
        });
    }

    private void translateNameSection(List<Text> segment) {
        segment.forEach(t -> {
            String _string = t.getString();
            if(_string.matches(".+ RuneÀ*")) {
                resultText.append(Text.translatable("wytr.item.misc." + normalizeStringForKey(_string))
                        .setStyle(t.getSiblings().getFirst().getStyle()));
            }
            else if(_string.contains("Use this item")) {
                wrapLine(Text.translatable("wytr.item.misc.Rune.desc").setStyle(GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.contains("or craft a")) return;
            else resultText.append(t);
        });
    }
}
