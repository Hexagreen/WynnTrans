package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class TotemHealing extends WynnDisplayText {

	private final Text timer;
	private final String heal;

	public static boolean typeChecker(Text text) {
		if(!text.getSiblings().isEmpty()) return false;
		return text.getString().matches("§c\\d+s\\n§c\\+\\d+❤§7/s");
	}

	public TotemHealing(Text text) {
		super(text);
		String[] split = text.getString().split("\\n");
		this.timer = new Timer(Text.literal(split[0])).text();
		this.heal = split[1];
	}

	@Override
	protected String setParentKey() {
		return null;
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty();
		resultText.append(timer).append("\n").append(heal);
	}
}
