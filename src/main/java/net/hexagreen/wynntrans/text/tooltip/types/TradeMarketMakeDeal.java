package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class TradeMarketMakeDeal extends WynnTooltipText implements ITooltipSplitter {
    private final boolean isBuyingOrder;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.contains("\uE014") && !WynnTrans.currentScreen.contains("\uE015")) return false;
        return Items.match(texts.getFirst().getString()) != null;
    }

    public TradeMarketMakeDeal(List<Text> texts) {
        super(texts);
        this.isBuyingOrder = WynnTrans.currentScreen.contains("\uE015");
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        switch(Items.match(getSibling(0).getString())) {
            case ITEM_SLOT -> buildItemSlot();
            case WAIT_ITEM -> buildWaitItem();
            case PRICE_CHECK -> buildPriceCheck();
            case SILVERBULL -> buildSilverbull();
            case SET_PRICE -> buildSetPrice();
            case DEC_AMOUNT -> buildDecAmount();
            case SET_AMOUNT -> buildSetAmount();
            case INC_AMOUNT -> buildIncAmount();
            case ORDER_SUMMARY -> buildOrderSummary();
        }
    }

    private void buildItemSlot() {
        resultText.append(Text.translatable("wytr.tooltip.ui.emptyItemSlot").formatted(Formatting.BOLD, Formatting.GRAY))
                .append(Text.translatable("wytr.tooltip.ui.emptyItemSlot.trade").formatted(Formatting.GRAY));
    }

    private void buildWaitItem() {
        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.waitItem").formatted(Formatting.BOLD, Formatting.GRAY))
                .append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.tradeMarket.order.waitItem.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER).append(Text.translatable("wytr.tooltip.ui.emptyItemSlot.trade").formatted(Formatting.GRAY));
    }

    private void buildPriceCheck() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Price Check")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.priceCheck")
                        .formatted(Formatting.BOLD, Formatting.DARK_AQUA));
            }
            else if(_string.equals("Price data is only available")) {
                wrapLine(Text.translatable("wytr.tooltip.tradeMarket.order.priceCheck.wait").formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);

            }
            else if(_string.equals("These are the calculated prices")) {
                wrapLine(Text.translatable("wytr.tooltip.tradeMarket.order.priceCheck.desc").formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);

            }
            else if(_string.contains("Recommended Price:")) {
                for(Text t : seg) {
                    Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
                    String _s = siblings.removeFirst().getString();
                    if(_s.contains("Recommended")) {
                        Object[] args = siblings.stream().map(this::trimNumberText).toArray(Object[]::new);
                        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.priceCheck.recommend", args)
                                .formatted(Formatting.LIGHT_PURPLE));
                    }
                    else if(_s.contains("Quick Sell")) {
                        Object[] args = siblings.stream().map(this::trimNumberText).toArray(Object[]::new);
                        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.priceCheck.quickSell", args)
                                .formatted(Formatting.GOLD));
                    }
                    else if(_s.contains("Instant Buy")) {
                        Object[] args = siblings.stream().map(this::trimNumberText).toArray(Object[]::new);
                        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.priceCheck.quickBuy", args)
                                .formatted(Formatting.GOLD));
                    }
                    else resultText.append(t);
                }
            }
            else if(_string.matches("(Highest Buy|Cheapest Sell) Offer: .+")) {
                for(Text t : seg) {
                    Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
                    String _s = siblings.removeFirst().getString();
                    if(_s.contains("Highest Buy")) {
                        Object[] args = siblings.stream().map(this::trimNumberText).toArray(Object[]::new);
                        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.priceCheck.maxBuy", args)
                                .formatted(Formatting.GRAY));
                    }
                    else if(_s.contains("Cheapest Sell")) {
                        Object[] args = siblings.stream().map(this::trimNumberText).toArray(Object[]::new);
                        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.priceCheck.minSell", args)
                                .formatted(Formatting.GRAY));
                    }
                    else resultText.append(t);
                }
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildSilverbull() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Trade Silverbull Shares")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.silverbull")
                        .formatted(Formatting.BOLD, Formatting.AQUA));
            }
            else if(_string.equals("Only tradable shares can be")) {
                wrapLine(Text.translatable("wytr.tooltip.tradeMarket.order.silverbull.desc").formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Available Shares")) {
                for(Text t : seg) {
                    Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
                    Text firstText = siblings.removeFirst();
                    String _s = firstText.getString();
                    if(_s.equals("- ")) _s = siblings.removeFirst().getString();
                    if(_s.contains("Available Shares")) {
                        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.silverbull.list")
                                .formatted(Formatting.AQUA));
                    }
                    else if(_s.contains("✔")) {
                        Text num = siblings.getLast();
                        Text line = Text.empty().append(firstText)
                                .append(Text.translatable("wytr.tooltip.tradeMarket.order.silverbull.notTradable", num)
                                        .formatted(Formatting.GREEN));
                        resultText.append(line);
                    }
                    else if(_s.contains("✖")) {
                        Text num = siblings.getLast();
                        Text line = Text.empty().append(firstText)
                                .append(Text.translatable("wytr.tooltip.tradeMarket.order.silverbull.notTradable", num)
                                        .formatted(Formatting.RED));
                        resultText.append(line);
                    }
                    else resultText.append(t);
                }
            }
            else if(_string.contains("Not enough tradable shares")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.silverbull.notEnough")
                        .formatted(Formatting.RED));
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildSetPrice() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Set Price")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.setPrice")
                        .formatted(Formatting.BOLD, Formatting.GREEN));
            }
            else if(_string.contains("Defines the price")) {
                String key = isBuyingOrder ? "wytr.tooltip.tradeMarket.order.setPrice.buyDesc" : "wytr.tooltip.tradeMarket.order.setPrice.sellDesc";
                wrapLine(Text.translatable(key).formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Current Price:")) {
                for(Text t : seg) {
                    Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
                    Text firstText = siblings.removeFirst();
                    String _s = firstText.getString();
                    if(_s.equals("- ")) _s = siblings.removeFirst().getString();
                    if(_s.contains("Current Price:")) {
                        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.setPrice.current")
                                .formatted(Formatting.GREEN));
                    }
                    else if(_s.contains("Per Unit:")) {
                        Object[] args = siblings.stream().map(this::trimNumberText).toArray(Object[]::new);
                        Text line = Text.empty().append(firstText)
                                .append(Text.translatable("wytr.tooltip.tradeMarket.order.setPrice.current.unit", args)
                                        .formatted(Formatting.GRAY));
                        resultText.append(line);
                    }
                    else if(_s.contains("Total:")) {
                        Object[] args = siblings.stream().map(this::trimNumberText).toArray(Object[]::new);
                        Text line = Text.empty().append(firstText)
                                .append(Text.translatable("wytr.tooltip.tradeMarket.order.setPrice.current.total", args)
                                        .formatted(Formatting.GRAY));
                        resultText.append(line);
                    }
                    else resultText.append(t);
                }
            }
            else if(_string.contains("Click to set")) {
                Text line = Text.empty().append(seg.getFirst().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable("wytr.func.clickToSet").formatted(Formatting.GREEN));
                resultText.append(line);
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildSetAmount() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Set Amount")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.setAmount")
                        .formatted(Formatting.BOLD, Formatting.DARK_GREEN));
                String key = isBuyingOrder ? "wytr.tooltip.tradeMarket.order.setAmount.buyDesc" : "wytr.tooltip.tradeMarket.order.setAmount.sellDesc";
                wrapLine(Text.translatable(key).formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Current:")) {
                Deque<Text> siblings = new ArrayDeque<>(seg.getFirst().getSiblings());
                Text num = siblings.getLast();
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.setAmount.current", num)
                        .formatted(Formatting.DARK_GREEN));
            }
            else if(_string.contains("Click to")) {
                Text line = Text.empty().append(seg.getFirst().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable("wytr.func.clickToSet").formatted(Formatting.GREEN));
                resultText.append(line);
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildDecAmount() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Decrease Amount")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.decAmount")
                        .formatted(Formatting.BOLD, Formatting.DARK_GREEN));
            }
            else if(_string.contains("Decrease the amount")) {
                String key = isBuyingOrder ? "wytr.tooltip.tradeMarket.order.decAmount.buyDesc" : "wytr.tooltip.tradeMarket.order.decAmount.sellDesc";
                wrapLine(Text.translatable(key).formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Current:")) {
                Deque<Text> siblings = new ArrayDeque<>(seg.getFirst().getSiblings());
                Text num = siblings.getLast();
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.setAmount.current", num)
                        .formatted(Formatting.DARK_GREEN));
                wrapLine(Text.translatable("wytr.tooltip.tradeMarket.order.setAmount.manualDesc").formatted(Formatting.DARK_GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Click to")) {
                for(Text t : seg) {
                    String _s = t.getString();
                    Text line;
                    if(_s.contains("by 1")) {
                        line = Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.tradeMarket.order.decAmount.dec1").formatted(Formatting.GREEN));
                    }
                    else if(_s.contains("by 5")) {
                        line = Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.tradeMarket.order.decAmount.dec5").formatted(Formatting.GREEN));
                    }
                    else line = t;
                    resultText.append(line);
                }
            }
            else if(_string.contains("Cannot decrease anymore")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.decAmount.min").formatted(Formatting.RED));
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildIncAmount() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Increase Amount")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.incAmount")
                        .formatted(Formatting.BOLD, Formatting.DARK_GREEN));
            }
            else if(_string.contains("Increase the amount")) {
                String key = isBuyingOrder ? "wytr.tooltip.tradeMarket.order.incAmount.buyDesc" : "wytr.tooltip.tradeMarket.order.incAmount.sellDesc";
                wrapLine(Text.translatable(key).formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Current:")) {
                Deque<Text> siblings = new ArrayDeque<>(seg.getFirst().getSiblings());
                Text num = siblings.getLast();
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.setAmount.current", num)
                        .formatted(Formatting.DARK_GREEN));
                wrapLine(Text.translatable("wytr.tooltip.tradeMarket.order.setAmount.manualDesc").formatted(Formatting.DARK_GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(_string.contains("Click to")) {
                for(Text t : seg) {
                    String _s = t.getString();
                    Text line;
                    if(_s.contains("by 1")) {
                        line = Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.tradeMarket.order.incAmount.dec1").formatted(Formatting.GREEN));
                    }
                    else if(_s.contains("by 5")) {
                        line = Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.tradeMarket.order.incAmount.dec5").formatted(Formatting.GREEN));
                    }
                    else line = t;
                    resultText.append(line);
                }
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildOrderSummary() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        new ITradableItem() {
        }.insertPriceSection(segments, resultText::append, (seg, i, lastIndex) -> {
            String _string = seg.getFirst().getString();
            if(_string.matches("(Buy|Sell) Order Summary")) {
                String key = isBuyingOrder ? "wytr.tooltip.tradeMarket.order.summary.buyOrder" : "wytr.tooltip.tradeMarket.order.summary.sellOrder";
                resultText.append(Text.translatable(key).formatted(Formatting.BOLD, Formatting.GREEN));
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.summary.desc")
                        .formatted(Formatting.DARK_GRAY));
            }
            else if(_string.matches("(Buying|Selling):")) {
                String key = isBuyingOrder ? "wytr.tooltip.tradeMarket.order.summary.buyingItem" : "wytr.tooltip.tradeMarket.order.summary.sellingItem";
                resultText.append(Text.translatable(key).formatted(Formatting.GREEN));
                Deque<Text> siblings = new ArrayDeque<>(seg.getLast().getSiblings());
                siblings.removeFirst();
                String[] split = siblings.removeFirst().getString().split("x ", 2);
                String _amount = split[0];
                siblings.addFirst(Text.literal(split[1]).formatted(Formatting.GRAY));
                Text amount = Text.translatable("wytr.unit.quantity", _amount).formatted(Formatting.GRAY);
                MutableText _itemName = Text.empty();
                siblings.forEach(_itemName::append);
                Text itemName = new ItemName(_itemName).noAdditionMode().textAsMutable();
                Text line = Text.translatable("wytr.tooltip.tradeMarket.order.summary.tradingItem", amount, itemName)
                        .formatted(Formatting.GREEN);
                resultText.append(line);
            }
            else if(_string.equals("Not enough items to sell")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.summary.overSell").formatted(Formatting.RED));
            }
            else if(_string.equals("Cannot afford")) {
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.order.summary.overBuy").formatted(Formatting.RED));
            }
            else if(_string.contains("Click to place")) {
                String key = isBuyingOrder ? "wytr.tooltip.tradeMarket.order.summary.postBuyOrder" : "wytr.tooltip.tradeMarket.order.summary.postSellOrder";
                Text line = Text.empty().append(seg.getFirst().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable(key).formatted(Formatting.GREEN));
                resultText.append(line);
            }
            else seg.forEach(resultText::append);
            if(i != lastIndex) resultText.append(SPLITTER);
        });

    }

    private Text trimNumberText(Text text) {
        String s = text.getString();
        Style st = text.getStyle();
        return Text.literal(s.trim()).setStyle(st);
    }

    private enum Items {
        ITEM_SLOT("Empty Item Slot"),
        WAIT_ITEM("Waiting for an Item"),
        PRICE_CHECK("Price Check"),
        SILVERBULL("Trade Silverbull Shares"),
        SET_PRICE("Set Price"),
        DEC_AMOUNT("Decrease Amount"),
        SET_AMOUNT("Set Amount"),
        INC_AMOUNT("Increase Amount"),
        ORDER_SUMMARY("(Buy|Sell) Order Summary");

        private final String styled;

        static Items match(String string) {
            return Arrays.stream(values())
                    .filter(i -> string.matches(i.styled))
                    .findAny().orElse(null);
        }

        Items(String styled) {
            this.styled = styled;
        }
    }
}
