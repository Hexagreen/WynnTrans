package net.hexagreen.wynntrans.text.tooltip;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.function.Consumer;

public interface ITradableItem extends ITooltipSplitter {

    Style GOLD = Style.EMPTY.withColor(Formatting.GOLD);
    MutableText PRICE_TEXT = Text.translatable("wytr.func.price").setStyle(GOLD);

    default void insertPriceSection(List<List<Text>> segments, Consumer<Text> appender, IterationStep step) {
        boolean priceSectionFound = false;
        for(int i = 0, size = segments.size(); i < size; i++) {
            List<Text> seg = segments.get(i);
            if(!priceSectionFound && seg.getFirst().getString().contains("Price:")) {
                priceSectionFound = true;
                seg.forEach(t -> {
                    if(t.getString().equals("Price:")) appender.accept(PRICE_TEXT);
                    else appender.accept(t);
                });
                if(i != size - 1) appender.accept(SPLITTER);
                continue;
            }
            else if(seg.getFirst().getString().equals("Click to buy 1")) {
                seg.forEach(t -> {
                    switch(t.getString()) {
                        case "Click to buy 1" -> appender.accept(Text.translatable("wytr.func.clickToBuy"));
                        case "Shift + Left-Click to buy 5" ->
                                appender.accept(Text.translatable("wytr.func.clickToBuy5"));
                        case "Shift + Right-Click to buy All" ->
                                appender.accept(Text.translatable("wytr.func.clickToBuyAll"));
                    }
                });
                if(i != size - 1) appender.accept(SPLITTER);
                continue;
            }
            step.process(seg, i);
        }
    }

    @FunctionalInterface
    interface IterationStep {
        void process(List<Text> currentSegment, int index);
    }
}
