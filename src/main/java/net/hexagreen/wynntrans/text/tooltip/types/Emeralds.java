package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Emeralds extends WynnTooltipText implements ITradableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("§aLiquid Emerald|§aEmerald Block|§aEmerald");
    }

    public static MutableText getTranslatedItemName(Text text) {
        if(!text.getString().matches("Liquid Emerald|Emerald Block|Emerald")) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        Emeralds emeralds = new Emeralds(textBowl);
        emeralds.translateNameSection(emeralds.inputText.getSiblings());
        return emeralds.resultText.getSiblings().getFirst().copy();
    }

    public Emeralds(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        insertPriceSection(segments, resultText::append, (seg, i) -> {
            if(i == 0) {
                translateNameSection(seg);
            }
            else {
                seg.forEach(resultText::append);
            }
            if(i != segments.size() - 1) resultText.append(SPLITTER);
        });
    }

    private void translateNameSection(List<Text> segment) {
        segment.forEach(t -> {
            String _string = t.getString();
            if(_string.matches("Liquid Emerald|Emerald Block|Emerald")) {
                resultText.append(Text.translatable("wytr.item.misc." + normalizeStringForKey(_string)).setStyle(t.getSiblings().getFirst().getStyle()));
            }
            else if(_string.equals("Currency")) {
                resultText.append(Text.translatable("wytr.item.misc.Emerald.tag").setStyle(GRAY));
            }
            else if(_string.contains("Transfer ")) {
                Text a = t.getSiblings().get(1);
                Text b = t.getSiblings().get(3);
                resultText.append(Text.translatable("wytr.item.misc.Emerald.transfer", a, b).setStyle(GRAY));
            }
            else resultText.append(t);
        });
    }
}
