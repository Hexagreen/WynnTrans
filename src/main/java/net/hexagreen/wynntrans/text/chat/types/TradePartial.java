package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class TradePartial extends WynnSystemText {

	private final boolean buyingMode;
	private final String tradeAmount;
	private final Text item;

	public TradePartial(Text text, Pattern regex) {
		super(text, regex);
		this.buyingMode = text.getString().contains(" has been Bought.");
		this.tradeAmount = matcher.group(1);
		this.item = parseItem();
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.tradePartial.";
	}

	@Override
	protected void build() {
		resultText = Text.empty();
		if(buyingMode) {
			resultText.append(newTranslateWithSplit(parentKey + "buy", tradeAmount, item));
			return;
		}
		resultText.append(newTranslateWithSplit(parentKey + "sell", tradeAmount, item));
	}

	private Text parseItem() {
		MutableText item = Text.empty().append(header).setStyle(getStyle());
		String content = getContentString(1).replaceAll("À§d.+?$", "");
		item.append(Text.literal(content).setStyle(getStyle(1)));
		return item;
	}
}
