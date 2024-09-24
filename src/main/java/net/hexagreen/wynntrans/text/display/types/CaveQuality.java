package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CaveQuality extends WynnDisplayText {

	private final String bar;

	public CaveQuality(Text text) {
		super(text);
		this.bar = text.getString().split("\\n")[1];
	}

	public static boolean typeChecker(Text text) {
		return text.getString().matches("^§7Loot Quality\\n.+");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "display.caveQuality";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty();
		resultText.append(newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n").append(bar);
	}
}
