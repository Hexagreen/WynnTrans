package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class TradeMarketOrderView extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.contains("\uE012")) return false;
        return texts.getFirst().getString().matches("Cancel Order|Withdraw Items");
    }

    public TradeMarketOrderView(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        switch(getSibling(0).getString()) {
            case "Cancel Order" -> buildCancelOrder();
            case "Withdraw Items" -> buildWithdraw();
        }
    }

    private void buildCancelOrder() {
        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.view.cancel").formatted(Formatting.RED));
        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.tradeMarket.view.cancel.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        resultText.append(Text.empty().append(getSibling(-1).getSiblings().getFirst())
                .append(" ").append(Text.translatable("wytr.tooltip.tradeMarket.view.click").formatted(Formatting.RED)));
    }

    private void buildWithdraw() {
        resultText.append(Text.translatable("wytr.tooltip.tradeMarket.view.withdraw").formatted(Formatting.GREEN));
        resultText.append(SPLITTER);
        wrapLine(Text.translatable("wytr.tooltip.tradeMarket.view.withdraw.desc").formatted(Formatting.GRAY), 150)
                .forEach(resultText::append);
        resultText.append(SPLITTER);
        resultText.append(Text.empty().append(getSibling(-1).getSiblings().getFirst())
                .append(" ").append(Text.translatable("wytr.tooltip.tradeMarket.view.click").formatted(Formatting.GREEN)));
    }
}
