package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class TradePartial extends WynnChatText {
    private final boolean buyingMode;
    private final String tradeAmount;
    private final Text item;

    public TradePartial(Text text, Pattern regex) {
        super(text, regex);
        this.buyingMode = text.getString().contains(" has been purchased.");
        this.tradeAmount = matcher.group(1);
        this.item = parseItem();
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "tradePartial";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        if(buyingMode) {
            resultText.append(newTranslate(parentKey + "buy", tradeAmount, item));
            return;
        }
        resultText.append(newTranslate(parentKey + "sell", tradeAmount, item));
    }

    private Text parseItem() {
        MutableText item = Text.empty();
        for(int i = 1; i < inputText.getSiblings().size() - 1; i++) {
            item.append(getSibling(i));
        }
        return item;
    }
}
