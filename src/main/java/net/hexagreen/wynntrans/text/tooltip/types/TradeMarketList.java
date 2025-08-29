package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class TradeMarketList extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.contains("\uE011")) return false;
        return Items.match(texts.getFirst().getString()) != null;
    }

    public TradeMarketList(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        switch(Items.match(getSibling(0).getString())) {
            case SELL_ITEM -> buildSellItem();
            case VIEW_TRADE -> buildViewTrade();
            case SEARCH -> buildSearch();
            case REVEAL -> buildReveal();
            case LEVEL -> buildLevelFilter();
            case SORT -> buildSort();
            case PERSONAL_STORE -> buildPersonalStore();
        }
    }

    private void buildSellItem() {
        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.sell").formatted(Formatting.BOLD, Formatting.GREEN));
        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.tradeMarket.sell.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        resultText.append(Text.empty().append(getSibling(-1).getSiblings().getFirst())
                .append(" ")
                .append(Text.translatable("wytr.func.clickToOpen").formatted(Formatting.GREEN)));
    }

    private void buildViewTrade() {
        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.viewTrade").formatted(Formatting.BOLD, Formatting.GREEN));
        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.tradeMarket.viewTrade.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        resultText.append(Text.empty().append(getSibling(-1).getSiblings().getFirst())
                .append(" ")
                .append(Text.translatable("wytr.func.clickToOpen").formatted(Formatting.GREEN)));
    }

    private void buildSearch() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, size = segments.size(); i < size; i++) {
            List<Text> seg = segments.get(i);
            if(i == 0)
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.search").formatted(Formatting.BOLD, Formatting.YELLOW));
            else if(i == 1) {
                wrapLine(Text.translatable("wytr.tooltip.tradeMarket.search.desc").formatted(Formatting.GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(i == 2) {
                for(Text t : seg) {
                    Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
                    MutableText line = Text.empty().append(siblings.removeFirst()).append(" ");
                    Text filterName = siblings.removeFirst();
                    if(filterName.getString().contains("Name Contains")) {
                        line.append(Text.translatable("wytr.tooltip.tradeMarket.search.nameFilter", siblings.removeFirst()).formatted(Formatting.GRAY));
                    }
                    else if(filterName.getString().contains("No filters")) {
                        line.append(Text.translatable("wytr.tooltip.tradeMarket.search.empty").formatted(Formatting.DARK_GRAY));
                    }
                    else {
                        line.append(TradeMarketFilter.findFilterName(filterName));
                    }
                    resultText.append(line);
                }
            }
            else if(i == 3) {
                resultText.append(Text.empty().append(seg.getFirst().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable("wytr.tooltip.tradeMarket.search.nameSearch").formatted(Formatting.GREEN)));
                resultText.append(Text.empty().append(seg.getLast().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable("wytr.tooltip.tradeMarket.search.setFilter").formatted(Formatting.GREEN)));
            }
            else segments.get(i).forEach(resultText::append);
            if(i != size - 1) resultText.append(SPLITTER);
        }
    }

    private void buildReveal() {
        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.revealItems").formatted(Formatting.BOLD, Formatting.LIGHT_PURPLE));
        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.tradeMarket.revealItems.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        resultText.append(Text.empty().append(getSibling(-1).getSiblings().getFirst())
                .append(" ")
                .append(Text.translatable("wytr.func.clickToOpen").formatted(Formatting.GREEN)));
    }

    private void buildLevelFilter() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, size = segments.size(); i < size; i++) {
            if(i == 0)
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.levelFilter").formatted(Formatting.BOLD, Formatting.GOLD));
            else if(i == 1) {
                wrapLine(Text.translatable("wytr.tooltip.tradeMarket.levelFilter.desc").formatted(Formatting.DARK_GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(i == 2) {
                for(Text _line : segments.get(i)) {
                    MutableText line = Text.empty();
                    Deque<Text> siblings = new ArrayDeque<>(_line.getSiblings());
                    line.append(siblings.removeFirst());
                    Text filter = siblings.removeFirst();
                    if(filter.getString().equals("Show All")) {
                        line.append(Text.translatable("wytr.tooltip.tradeMarket.levelFilter.showAll").setStyle(filter.getStyle()));
                    }
                    else {
                        String num = filter.getString().replaceFirst("Lv\\. ", "");
                        line.append(Text.translatable("wytr.unit.level", num).setStyle(filter.getStyle()));
                    }
                    siblings.forEach(line::append);
                    resultText.append(line);
                }
            }
            else if(i == 3) {
                for(Text t : segments.get(i)) {
                    String _s = t.getString();
                    if(_s.contains("forward")) {
                        resultText.append(Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.ui.forward").formatted(Formatting.GREEN)));
                    }
                    else if(_s.contains("backward")) {
                        resultText.append(Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.ui.backward").formatted(Formatting.GREEN)));
                    }
                    else if(_s.contains("default")) {
                        resultText.append(Text.empty().append(t.getSiblings().getFirst())
                                .append(" ")
                                .append(Text.translatable("wytr.tooltip.tradeMarket.levelFilter.default").formatted(Formatting.GOLD)));
                    }
                    else resultText.append(t);
                }
            }
            else segments.get(i).forEach(resultText::append);
            if(i != size - 1) resultText.append(SPLITTER);
        }
    }

    private void buildSort() {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, size = segments.size(); i < size; i++) {
            if(i == 0)
                resultText.append(Text.translatable("wytr.tooltip.tradeMarket.sort").formatted(Formatting.BOLD, Formatting.GOLD));
            else if(i == 1) {
                wrapLine(Text.translatable("wytr.tooltip.tradeMarket.sort.desc").formatted(Formatting.DARK_GRAY), 150)
                        .forEach(resultText::append);
            }
            else if(i == 2) {
                new SimpleTooltip(segments.get(i)).textAsMutable().getSiblings().forEach(resultText::append);
            }
            else if(i == 3) {
                List<Text> seg = segments.get(i);
                resultText.append(Text.empty().append(seg.getFirst().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable("wytr.tooltip.ui.forward").formatted(Formatting.GREEN)));
                resultText.append(Text.empty().append(seg.getLast().getSiblings().getFirst())
                        .append(" ")
                        .append(Text.translatable("wytr.tooltip.ui.backward").formatted(Formatting.GREEN)));
            }
            else segments.get(i).forEach(resultText::append);
            if(i != size - 1) resultText.append(SPLITTER);
        }
    }

    private void buildPersonalStore() {
        String name = getSibling(0).getString().replaceFirst("'s? Store$", "");
        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.private", name).formatted(Formatting.AQUA));
        resultText.append(SPLITTER);
        resultText.append(getSibling(-1));
    }

    private enum Items {
        SELL_ITEM("Sell an Item"),
        VIEW_TRADE("View Trades"),
        SEARCH("Search and Filter"),
        REVEAL("Reveal Items"),
        LEVEL("Level Filter"),
        SORT("Sort Results"),
        PERSONAL_STORE(".+'s Store");

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
