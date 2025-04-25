package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TradePartial extends WynnSystemText {
    private final boolean buyingMode;
    private final String tradeAmount;
    private final Text item;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("(\\d+)x .+ has been (?:Bought|Sold)\\.$").matcher(text.getString()).find();
    }

    public TradePartial(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("(\\d+)x .+ has been (?:Bought|Sold)\\.$").matcher(inputText.getString().replaceAll("\n", ""));
        boolean ignore = matcher.find();
        this.buyingMode = text.getString().contains(" has been Bought.");
        this.tradeAmount = matcher.group(1);
        this.item = parseItem();
    }

    @Override
    protected int setLineWrappingWidth() {
        return (int) ISpaceProvider.CHAT_HUD_WIDTH / 2;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.tradePartial.";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        if(buyingMode) {
            resultText.append(Text.translatable(translationKey + "buy", tradeAmount, item).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            return;
        }
        resultText.append(Text.translatable(translationKey + "sell", tradeAmount, item).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }

    private Text parseItem() {
        MutableText item = Text.empty().setStyle(getStyle());
        String content = getContentString(1).replaceAll("À§d.+?$", "");
        item.append(Text.literal(content).setStyle(getStyle(1)));
        return item;
    }
}
