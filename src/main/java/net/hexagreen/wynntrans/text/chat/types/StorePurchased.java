package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class StorePurchased extends WynnChatText implements ISpaceProvider {

	public StorePurchased(Text text, Pattern regex) {
		super(text, regex);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.storePurchased";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		Text t0 = newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true));
		Text t1 = newTranslate(parentKey + ".1").setStyle(Style.EMPTY.withColor(Formatting.GRAY));

		resultText = Text.empty();
		resultText.append(getCenterIndent(t0).append(t0)).append("\n").append(getCenterIndent(t1).append(t1));
	}
}
