package net.hexagreen.wynntrans.enums;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum Bombs {
	COMBAT_XP(Text.translatable("wytr.bomb.name.combat"),
			Text.translatable("wytr.bomb.desc.combat").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
			Text.translatable("wytr.bomb.time.combat").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
	PROFESSION_XP(Text.translatable("wytr.bomb.name.profXp"),
			Text.translatable("wytr.bomb.desc.profXp").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
			Text.translatable("wytr.bomb.time.profXp").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
	PROFESSION_SPEED(Text.translatable("wytr.bomb.name.profSpeed"),
			Text.translatable("wytr.bomb.desc.profSpeed").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
			Text.translatable("wytr.bomb.time.profSpeed").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
	ITEM(Text.translatable("wytr.bomb.name.item"),
			Text.translatable("wytr.bomb.desc.item").setStyle(Style.EMPTY.withColor(Formatting.WHITE)),
			null),
	SOUL_POINT(Text.translatable("wytr.bomb.name.soulPoint"),
			Text.translatable("wytr.bomb.desc.soulPoint").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
			Text.translatable("wytr.bomb.time.soulPoint").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
	DUNGEON(Text.translatable("wytr.bomb.name.dungeon"),
			Text.translatable("wytr.bomb.desc.dungeon",
							Text.translatable("wytr.bomb.time.dungeon").setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
					.setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)),
			null),
	LOOT(Text.translatable("wytr.bomb.name.loot"),
			Text.translatable("wytr.bomb.desc.loot").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
			Text.translatable("wytr.bomb.time.loot").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
	INGREDIENT(Text.translatable("wytr.bomb.name.ingredient"),
			Text.translatable("wytr.bomb.desc.ingredient").setStyle(Style.EMPTY.withColor(Formatting.WHITE)),
			Text.translatable("wytr.bomb.time.ingredient").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)
					.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/claimingredientbomb"))
					.withUnderline(true))),
	PARTY(Text.translatable("wytr.bomb.name.party"),
			Text.translatable("wytr.bomb.desc.party").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
			Text.translatable("wytr.bomb.time.party").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));

	private final Text bombName;
	private final Text bombDescription;
	private final Text bombTime;

	public static Bombs findBomb(String bombName) {
		if(bombName.contains("Combat XP")) {
			return COMBAT_XP;
		}
		else if(bombName.contains("Profession XP")) {
			return PROFESSION_XP;
		}
		else if(bombName.contains("Profession Speed")) {
			return PROFESSION_SPEED;
		}
		else if(bombName.contains("Item")) {
			return ITEM;
		}
		else if(bombName.contains("Soul Point")) {
			return SOUL_POINT;
		}
		else if(bombName.contains("Dungeon")) {
			return DUNGEON;
		}
		else if(bombName.contains("Loot")) {
			return LOOT;
		}
		else if(bombName.contains("Ingredient")) {
			return INGREDIENT;
		}
		else if(bombName.contains("Party")) {
			return PARTY;
		}
		return null;
	}

	Bombs(Text bombName, Text bombDesc, Text bombTime) {
		this.bombName = bombName;
		this.bombDescription = bombDesc;
		this.bombTime = bombTime;
	}

	public MutableText getBombName() {
		return bombName.copy();
	}

	public Text getBombDescription() {
		return bombDescription;
	}

	public Text getBombTime() {
		return bombTime;
	}
}
