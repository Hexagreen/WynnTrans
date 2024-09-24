package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class PouchSold extends WynnChatText {

	private final String amount;
	private final String emerald;

	public PouchSold(Text text, Pattern regex) {
		super(text, regex);
		this.amount = matcher.group(1);
		this.emerald = matcher.group(2);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.pouchSold";
	}

	@Override
	protected void build() {
		resultText = Text.empty();
		resultText.append(newTranslate(parentKey, amount, emerald).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
	}
}
