package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class TradeFinish extends WynnSystemText {
    private final boolean buyingMode;
    private final Text item;

    public TradeFinish(Text text, Pattern regex) {
        super(text, regex);
        this.buyingMode = text.getString().contains("Finished buying ");
        this.item = parseItem();
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.tradeFinish.";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append(header).setStyle(getStyle());
        if(buyingMode) {
            resultText.append(newTranslateWithSplit(parentKey + "buy", item).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            return;
        }
        resultText.append(newTranslateWithSplit(parentKey + "sell", item).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }

    private Text parseItem() {
        MutableText item = Text.empty();
        if(getSiblings().size() == 2) {
            String content = getContentString(1).replaceAll("\\n|À\\.", "");
            item.append(Text.literal(content).setStyle(getStyle(1)));
        }
        else {
            String content = getContentString(0).replaceAll("^.+ §5|À\\.", "");
            item.append(Text.literal(content));
        }
        return item;
    }
}
