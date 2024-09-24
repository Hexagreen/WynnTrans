package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class PetName extends WynnDisplayText {

	public PetName(Text text) {
		super(text);
	}

	public static boolean typeChecker(Text text) {
		if(!text.getSiblings().isEmpty()) return false;
		return text.getString().matches("^§7.+'s .+\\n§2Lv\\. §a\\d+");
	}

	@Override
	protected String setParentKey() {
		return null;
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = inputText;
	}
}
