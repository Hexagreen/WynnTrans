package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

/**
 * This class will destroy income text
 */
public class EatThisText extends WynnChatText {

	public EatThisText(Text text, Pattern regex) {
		super(text, regex);
	}

	@Override
	protected String setParentKey() {
		return null;
	}

	@Override
	protected void build() {
		resultText = null;
	}
}
