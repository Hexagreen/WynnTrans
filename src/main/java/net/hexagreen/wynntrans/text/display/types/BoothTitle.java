package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class BoothTitle extends WynnDisplayText {

	private final String boothOwner;
	private final String boothDesc;

	public BoothTitle(Text text) {
		super(text);
		this.boothOwner = inputText.getString().replaceAll("'s Shop\\n(.|\\n)+", "").substring(2);
		this.boothDesc = inputText.getString().replaceAll(".+ Shop\\n", "");
	}

	public static boolean typeChecker(Text text) {
		return text.getString().contains("'s Shop\n");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "display.boothTitle";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty().append(newTranslate(parentKey, boothOwner).setStyle(Style.EMPTY.withColor(Formatting.AQUA))).append("\n").append(Text.literal(boothDesc).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
	}
}
