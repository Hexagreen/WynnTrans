package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BoothSetup extends WynnDisplayText {

	private static final Text icon = Text.literal("\uE000").setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:keybind")));

	public static boolean typeChecker(Text text) {
		return text.getString().contains("Click to set up booth");
	}

	public BoothSetup(Text text) {
		super(text);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "display.boothSetup";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty();
		resultText.append(icon).append(" ").append(newTranslate(parentKey));
	}
}
