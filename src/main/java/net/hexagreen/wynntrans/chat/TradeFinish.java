package net.hexagreen.wynntrans.chat;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class TradeFinish extends WynnChatText{
    private final boolean buyingMode;
    private final Text item;

    public TradeFinish(Text text, Pattern regex) {
        super(text, regex);
        this.buyingMode = text.getString().contains("Finished buying ");
        this.item = parseItem();
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "tradeFinish";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        if(buyingMode) {
            resultText.append(newTranslate(parentKey + "buy", item));
            return;
        }
        resultText.append(newTranslate(parentKey + "sell", item));
    }

    private Text parseItem() {
        MutableText item = Text.empty();
        for(int i = 1; i < inputText.getSiblings().size(); i++) {
            item.append(getSibling(i));
        }
        return item;
    }
}
