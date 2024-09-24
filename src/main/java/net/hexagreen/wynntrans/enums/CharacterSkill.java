package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

public enum CharacterSkill {
	STR(MutableText.of(new TranslatableTextContent("wytr.skill.strength", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN))),
	DEX(MutableText.of(new TranslatableTextContent("wytr.skill.dexterity", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW))),
	INT(MutableText.of(new TranslatableTextContent("wytr.skill.intelligence", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
	DEF(MutableText.of(new TranslatableTextContent("wytr.skill.defense", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.RED))),
	AGI(MutableText.of(new TranslatableTextContent("wytr.skill.agility", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

	private final Text stat;

	CharacterSkill(Text stat) {
		this.stat = stat;
	}

	public static Text getSkill(String name) {
		switch(name) {
			case "strength" -> {
				return STR.stat;
			}
			case "dexterity" -> {
				return DEX.stat;
			}
			case "intelligence" -> {
				return INT.stat;
			}
			case "defence" -> {
				return DEF.stat;
			}
			case "agility" -> {
				return AGI.stat;
			}
		}
		return null;
	}

	public Text getSkill() {
		return stat;
	}
}
