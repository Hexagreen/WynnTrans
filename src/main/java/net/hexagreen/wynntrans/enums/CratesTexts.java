package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Locale;
import java.util.regex.Pattern;

public class CratesTexts {

	private static final Text BAR = Text.literal("|||").setStyle(Style.EMPTY.withObfuscated(true).withBold(false).withColor(Formatting.BLACK));

	public enum Crates {
		COMMON("common", Text.translatable("wytr.crate.common").setStyle(Style.EMPTY.withColor(Formatting.WHITE))),
		RARE("rare", Text.translatable("wytr.crate.rare").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE))),
		EPIC("epic", Text.translatable("wytr.crate.epic").setStyle(Style.EMPTY.withColor(Formatting.GOLD))),
		GODLY("godly", Text.translatable("wytr.crate.godly").setStyle(Style.EMPTY.withColor(Formatting.RED))),
		BLACK_MARKET("Black Market",
				Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_RED))
						.append(BAR).append(Text.translatable("wytr.crate.blackMarket")).append(BAR));

		private final String gradeName;
		private final MutableText gradeText;
		private final Style gradeStyle;

		Crates(String gradeName, MutableText gradeText) {
			this.gradeName = gradeName;
			this.gradeText = gradeText;
			this.gradeStyle = gradeText.getStyle();
		}

		public static Crates find(String string) {
			return Arrays.stream(Crates.values()).filter(crateGrades -> crateGrades.match(string)).findFirst().orElse(COMMON);
		}

		public MutableText getGradeText() {
			return gradeText;
		}

		public Style getGradeStyle() {
			return gradeStyle;
		}

		private boolean match(String string) {
			return string.toLowerCase(Locale.ENGLISH).contains(this.gradeName);
		}
	}

	public enum RewardType {
		GEAR_SKIN("Gear Skin", Text.translatable("wytr.crate.gearSkin").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		ATTACK_EFFECT("Attack Effect", Text.translatable("wytr.crate.attackEffect").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		PLAYER_EFFECT("Player Effect", Text.translatable("wytr.crate.playerEffect").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		DISGUISE("Disguise", Text.translatable("wytr.crate.disguise").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		PET("Pet", Text.translatable("wytr.crate.pet").setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

		private final String typeName;
		private final Text typeText;

		RewardType(String typeName, Text typeText) {
			this.typeName = typeName;
			this.typeText = typeText;
		}

		public static RewardType find(String string) {
			return Arrays.stream(values()).filter(rewardType -> string.contains(rewardType.typeName)).findFirst().orElse(GEAR_SKIN);
		}

		public Text getTypeText() {
			return typeText;
		}
	}

	public enum RewardDescription {
		PLAYER_EFFECT_1(Pattern.compile("Give your character (?:a|the) "),
				Text.translatable("wytr.crateReward.playerEffect").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		PLAYER_EFFECT_2(Pattern.compile("effect around your character"),
				Text.translatable("wytr.crateReward.playerEffect").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		HELMET(Pattern.compile("Disguise your helmet"),
				Text.translatable("wytr.crateReward.helmet").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		WEAPON(Pattern.compile("Disguise your (wand|spear|bow|relik|dagger|staff)"),
				Text.translatable("wytr.crateReward.weapon").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		DISGUISE(Pattern.compile("Disguise yourself as"),
				Text.translatable("wytr.crateReward.disguise").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		ATTACK_EFFECT(Pattern.compile("effect whenever you hit"),
				Text.translatable("wytr.crateReward.attackEffect").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		PET_TOKEN(Pattern.compile("This token can be redeemed for"),
				Text.translatable("wytr.crateReward.petToken").setStyle(Style.EMPTY.withColor(Formatting.GRAY))),
		NULL(null, null);

		private final Pattern descRegex;
		private final Text translatedText;

		RewardDescription(Pattern descRegex, Text translatedText) {
			this.descRegex = descRegex;
			this.translatedText = translatedText;
		}

		private static RewardDescription find(Text text) {
			return Arrays.stream(RewardDescription.values()).filter(rewardDescription -> rewardDescription.descRegex.matcher(text.getString()).find()).findFirst().orElse(NULL);
		}

		public static Text findAndGet(Text text) {
			RewardDescription rewardDescription = find(text);
			if(rewardDescription != NULL) return rewardDescription.translatedText;
			else return text;
		}
	}
}