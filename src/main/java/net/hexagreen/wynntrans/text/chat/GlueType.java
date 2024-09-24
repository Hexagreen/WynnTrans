package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.text.chat.types.glue.*;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.regex.Pattern;

public enum GlueType {
	QUEST_CLEAR(Pattern.compile("^§. +\\[(?:Mini-)?Quest Completed]$"), QuestGlue.class),
	CAVE_CLEAR(Pattern.compile("^ +\\[Cave Completed]$"), CaveGlue.class),
	OBJECTIVE_CLEAR(Pattern.compile("^ +§[24]\\[Objective Completed]$"), ObjectiveGlue.class),
	LEVELUP(Pattern.compile("^§6 {32}§lLevel Up!$"), LevelUpGlue.class),
	CRATE_GET_PERSONAL(Pattern.compile("^§.§.You've gotten a .+§.§. reward!"), CrateGlue.class),
	AREA_DISCOVERY(Pattern.compile("^ *§[67]Area Discovered: §[ef]"), AreaDiscoveryGlue.class),
	SECRET_DISCOVERY(Pattern.compile("^ *§3Secret Discovery: "), SecretDiscoveryGlue.class),
	DUNGEON_COMPLETE(Pattern.compile("^§6Great job! You've completed the .+ Dungeon!"), DungeonCompleteGlue.class),
	REWARD_RECEIVED(Pattern.compile("^§[3b]You have received:"), RewardReceivedGlue.class),
	DEATH_ITEM_LOST(Pattern.compile("^§6By dying, you've lost:"), DeathItemLostGlue.class),
	WORLD_EVENT_FAILED(Pattern.compile("^(?:\uDAFF\uDFFC\uE00D\uDAFF\uDFFF\uE002\uDAFF\uDFFE|\uDAFF\uDFFC\uE001\uDB00\uDC06) You have failed "), WorldEventFailedGlue.class),
	WORLD_EVENT_COMPLETE(Pattern.compile("\uDAFF\uDFBE\uE016\uE00E\uE011\uE00B\uE003 \uE004\uE015\uE004\uE00D\uE013\uDB00\uDC02$"), WorldEventCompleteGlue.class),
	PARTY_INVITED(Pattern.compile("\\n +§eYou have been invited to join .+'s party!"), PartyInvitedGlue.class),
	STORE_PURCHASED(Pattern.compile("§6§l +Thank you for your purchase!"), StorePurchasedGlue.class),

	NO_TYPE(null, null);

	private final Pattern regex;
	private final Class<? extends TextGlue> glue;

	GlueType(Pattern regex, Class<? extends TextGlue> glue) {
		this.regex = regex;
		this.glue = glue;
	}

	public static GlueType findType(Text text) {
		return Arrays.stream(GlueType.values()).filter(glueType -> glueType.matchFullText(text)).findFirst().orElse(NO_TYPE);
	}

	public static TextGlue findAndGet(Text text) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
		GlueType find = findType(text);
		return find == NO_TYPE ? null : find.glue.cast(find.glue.getConstructor().newInstance());
	}

	public boolean matchFullText(Text text) {
		if(this == NO_TYPE) return false;
		return this.regex.matcher(text.getString()).find();
	}
}
