package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class Timer extends WynnDisplayText {

	private final Style color;

	public Timer(Text text) {
		super(text);
		this.color = parseStyleCode(inputText.getString().substring(0, 2));
	}

	public static boolean typeChecker(Text text) {
		return text.getString().matches("§.(\\d+)[ms]");
	}

	@Override
	protected String setParentKey() {
		return null;
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = ITime.translateTime(getContentString().replaceAll("§.", "")).setStyle(color);
	}
}
