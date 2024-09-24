package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ShoutLiteral extends WynnChatText {

	private final String name;
	private final String server;
	private final String body;

	public ShoutLiteral(Text text, Pattern regex) {
		super(text, regex);
		this.name = matcher.group(1);
		this.server = matcher.group(2);
		this.body = text.getString().replaceAll(regex.pattern(), "");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.shout";
	}

	@Override
	protected void build() {
		resultText = Text.empty();
		resultText.append(newTranslate(parentKey, name, server).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)));
		resultText.append(body).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
	}
}
