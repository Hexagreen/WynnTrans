package net.hexagreen.wynntrans.text.tooltip;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Consumer;

public interface ITradableItem extends ITooltipSplitter {
    MutableText PRICE_TEXT = Text.empty()
            .append(Text.literal("\uDAFF\uDFFC\uE00A\uDAFF\uDFFF\uE002\uDAFF\uDFFE")
                    .setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:chat/prefix")).withShadowColor(16777215)))
            .append(" ").append(Text.translatable("wytr.func.price"));
    MutableText PENDING = Text.translatable("wytr.tooltip.tradeMarket.status.pending").formatted(Formatting.BOLD, Formatting.GRAY);
    MutableText FULFILL = Text.translatable("wytr.tooltip.tradeMarket.status.fulfill").formatted(Formatting.BOLD, Formatting.GREEN);
    MutableText EXPIRE = Text.translatable("wytr.tooltip.tradeMarket.status.expire").formatted(Formatting.BOLD, Formatting.YELLOW);
    MutableText VIEW = Text.empty()
            .append(Text.literal("\uE000").setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:keybind")).withColor(Formatting.WHITE)))
            .append(" ")
            .append(Text.translatable("wytr.func.clickToView").formatted(Formatting.GREEN));

    default void insertPriceSection(List<List<Text>> segments, Consumer<Text> appender, IterationStep step) {
        insertPriceSection(segments, appender, step, (seg, i, l) -> {
        });
    }

    default void insertPriceSection(List<List<Text>> segments, Consumer<Text> appender, IterationStep step, IterationStep postStep) {
        boolean priceSectionFound = false;
        for(int i = 0, size = segments.size(); i < size; i++) {
            List<Text> seg = segments.get(i);
            if(i == 0) {
                List<Text> sib = seg.getLast().getSiblings();
                if(!sib.isEmpty() && sib.getFirst().getString().matches("Pending|Fulfilled|Expired")) {
                    Text _status = sib.getFirst(), _direction = sib.get(1), currentAmount = sib.get(2);
                    Text totalAmount = Text.literal(sib.getLast().getString().replaceAll("\\D", "")).formatted(Formatting.DARK_GRAY);
                    Text status = _status.getString().equals("Pending") ? PENDING : _status.getString().equals("Fulfilled") ? FULFILL : EXPIRE;
                    MutableText line = Text.empty().append(status);
                    if(_direction.getString().contains("Sold"))
                        line.append(Text.translatable("wytr.tooltip.tradeMarket.status.sold", currentAmount, totalAmount)
                                .formatted(Formatting.DARK_GRAY));
                    else
                        line.append(Text.translatable("wytr.tooltip.tradeMarket.status.bought", currentAmount, totalAmount)
                                .formatted(Formatting.DARK_GRAY));
                    seg.removeLast();
                    seg.addLast(line);
                }
            }
            else if(!priceSectionFound && seg.getFirst().getString().contains("\uDAFF\uDFFC\uE00A\uDAFF\uDFFF\uE002\uDAFF\uDFFE Price")) {
                priceSectionFound = true;
                seg.forEach(t -> {
                    if(t.getString().equals("\uDAFF\uDFFC\uE00A\uDAFF\uDFFF\uE002\uDAFF\uDFFE Price"))
                        appender.accept(PRICE_TEXT.copy().setStyle(t.getSiblings().getLast().getStyle()));
                    else appender.accept(t);
                });
                if(i != size - 1) appender.accept(SPLITTER);
                continue;
            }
            else if(seg.getFirst().getString().contains("Click to Buy")) {
                seg.forEach(t -> {
                    String string = t.getString();
                    MutableText line = Text.empty().append(t.getSiblings().getFirst()).append(" ");
                    if(string.contains("Click to Buy 1")) {
                        appender.accept(line.append(Text.translatable("wytr.func.clickToBuy1").formatted(Formatting.GREEN)));
                    }
                    else if(string.contains("Click to Buy 5")) {
                        appender.accept(line.append(Text.translatable("wytr.func.clickToBuy5").formatted(Formatting.GREEN)));
                    }
                    else if(string.contains("Click to Buy All")) {
                        appender.accept(line.append(Text.translatable("wytr.func.clickToBuyAll").formatted(Formatting.GREEN)));
                    }
                    else if(string.matches(". Click to Buy$")) {
                        appender.accept(line.append(Text.translatable("wytr.func.clickToBuy").formatted(Formatting.GREEN)));
                    }
                });
                if(i != size - 1) appender.accept(SPLITTER);
                postStep.process(seg, i, size - 1);
                continue;
            }
            else if(seg.getFirst().getString().matches("\uE000 Click to View")) {
                seg.forEach(t -> {
                    if(t.getString().contains("Click to View")) {
                        appender.accept(VIEW);
                    }
                    else appender.accept(t);
                });
                postStep.process(seg, i, size - 1);
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
