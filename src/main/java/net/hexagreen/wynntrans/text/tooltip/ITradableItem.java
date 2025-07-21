package net.hexagreen.wynntrans.text.tooltip;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public interface ITradableItem extends ITooltipSplitter {
    MutableText PRICE_TEXT = Text.translatable("wytr.func.price");
    MutableText PENDING = Text.translatable("wytr.tooltip.tradeMarket.status.pending").formatted(Formatting.BOLD, Formatting.GRAY);
    MutableText FULFILL = Text.translatable("wytr.tooltip.tradeMarket.status.fulfill").formatted(Formatting.BOLD, Formatting.GREEN);
    MutableText VIEW = Text.empty()
            .append(Text.literal("\uE000").setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:keybind")).withColor(Formatting.WHITE)))
            .append(" ")
            .append(Text.translatable("wytr.func.clickToView").formatted(Formatting.GREEN));

    default void insertPriceSection(List<List<Text>> segments, Consumer<Text> appender, IterationStep step) {
        boolean priceSectionFound = false;
        for(int i = 0, size = segments.size(); i < size; i++) {
            List<Text> seg = segments.get(i);
            if(i == 0) {
                List<Text> sib = seg.getLast().getSiblings();
                if(!sib.isEmpty() && sib.getFirst().getString().matches("Pending|Fulfilled")) {
                    Text _status = sib.getFirst(), _direction = sib.get(1), currentAmount = sib.get(2);
                    Text totalAmount = Text.literal(sib.getLast().getString().replaceAll("\\D", "")).formatted(Formatting.DARK_GRAY);
                    Text status = _status.getString().equals("Pending") ? PENDING : FULFILL;
                    MutableText line = Text.empty().append(status);
                    if(_direction.getString().contains("Sold"))
                        line.append(Text.translatable("wytr.tooltip.tradeMarket.status.sold", currentAmount, totalAmount)
                                .formatted(Formatting.DARK_GRAY));
                    else
                        line.append(Text.translatable("wytr.tooltip.tradeMarket.status.bought", currentAmount, totalAmount)
                                .formatted(Formatting.DARK_GRAY));
                    seg.removeLast();
                    seg.addLast(line);
                    segments.removeFirst();
                    segments.addFirst(seg);
                }
            }
            else if(!priceSectionFound && seg.getFirst().getString().contains("Price:")) {
                priceSectionFound = true;
                seg.forEach(t -> {
                    if(t.getString().equals("Price:"))
                        appender.accept(PRICE_TEXT.copy().setStyle(t.getSiblings().getFirst().getStyle()));
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
            else if(seg.getFirst().getString().matches("\uE000 Click to View")) {
                seg.forEach(t -> {
                    if(t.getString().contains("Click to View")) {
                        appender.accept(VIEW);
                    }
                    else appender.accept(t);
                });
                continue;
            }
            step.process(seg, i, size - 1);
        }
    }

    @FunctionalInterface
    interface IterationStep {
        void process(List<Text> currentSegment, int index, int lastIndex);
    }
}
