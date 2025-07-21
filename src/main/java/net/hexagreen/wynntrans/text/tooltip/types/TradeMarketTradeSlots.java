package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class TradeMarketTradeSlots extends WynnTooltipText implements ITooltipSplitter {
    Text L_CLICK_SELL = Text.empty()
            .append(Text.literal("\uE000").setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:keybind")).withColor(Formatting.WHITE)))
            .append(" ").append(Text.translatable("wytr.tooltip.tradeMarket.tradeSlot.createSell").formatted(Formatting.GREEN));
    Text R_CLICK_BUY = Text.empty()
            .append(Text.literal("\uE001").setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:keybind")).withColor(Formatting.WHITE)))
            .append(" ").append(Text.translatable("wytr.tooltip.tradeMarket.tradeSlot.createBuy").formatted(Formatting.GREEN));

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.contains("\uE013")) return false;
        return texts.getFirst().getString().matches("Available Slot");
    }

    public TradeMarketTradeSlots(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        buildAvailableSlot();
    }

    private void buildAvailableSlot() {
        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.tradeSlot.open").formatted(Formatting.GREEN));
        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.tradeMarket.tradeSlot.open.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER).append(L_CLICK_SELL).append(R_CLICK_BUY);
    }
}
