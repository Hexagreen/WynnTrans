package net.hexagreen.wynntrans.enums;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;

public enum Elements {
	NEUTRAL(Text.translatable("wytr.element.neutral").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
	EARTH(Text.translatable("wytr.element.earth").setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN))),
	THUNDER(Text.translatable("wytr.element.thunder").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))),
	WATER(Text.translatable("wytr.element.water").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
	FIRE(Text.translatable("wytr.element.fire").setStyle(Style.EMPTY.withColor(Formatting.RED))),
	AIR(Text.translatable("wytr.element.air").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

	private final Text element;

	Elements(Text element) {
		this.element = element;
	}

	public static Elements findElement(String string) {
		switch(string.toLowerCase(Locale.ENGLISH)) {
			case "earth" -> {
				return EARTH;
			}
			case "thunder" -> {
				return THUNDER;
			}
			case "water" -> {
				return WATER;
			}
			case "fire" -> {
				return FIRE;
			}
			case "air" -> {
				return AIR;
			}
			default -> {
				return NEUTRAL;
			}
		}
	}

	public Text getElement() {
		return element;
	}
}
