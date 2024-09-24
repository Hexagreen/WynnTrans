package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;
import java.util.regex.Pattern;

public class Disguising extends WynnChatText {

	private final boolean apply;
	private final Text target;

	public Disguising(Text text, Pattern regex) {
		super(text, regex);
		this.apply = matcher.group(1).equals("now");
		this.target = Text.literal(capitalizeFirstChar(matcher.group(2))).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.disguise.";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException {
		resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA));
		String key = apply ? parentKey + "on" : parentKey + "off";
		resultText.append(newTranslate(key, target));
	}

	private String capitalizeFirstChar(String input) {
		String body = input.substring(1);
		char head = input.toUpperCase(Locale.ENGLISH).charAt(0);

		return head + body;
	}
}
