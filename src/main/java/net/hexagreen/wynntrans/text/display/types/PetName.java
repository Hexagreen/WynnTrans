package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class PetName extends WynnDisplayText {

	public static boolean typeChecker(Text text) {
		if(!text.getSiblings().isEmpty()) return false;
		if(text.getString().matches("^§7.+'s .+\\n§2Lv\\. §a\\d+")) return true;
		if(text.getString().matches("^§2Lv\\. §a\\d+§f .+\\n§7\\[.+]")) return true;
		else return false;
	}

	public PetName(Text text) {
		super(text);
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
