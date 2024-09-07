package net.hexagreen.wynntrans.enums;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.hexagreen.wynntrans.text.display.types.*;
import net.minecraft.text.Text;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;

public enum DisplayType {
    DAMAGE_INDICATOR(DamageIndicator.class, DamageIndicator::typeChecker),
    COMBAT_XP_GAIN(CombatXPGain.class, CombatXPGain::typeChecker),
    CAVE_PROGRESS(CaveProgress.class, CaveProgress::typeChecker),
    CAVE_QUALITY(CaveQuality.class, CaveQuality::typeChecker),
    CAVE_TITLE(CaveTitle.class, CaveTitle::typeChecker),
    CAVE_REWARD(CaveReward.class, CaveReward::typeChecker),
    NPC_NAME_LITERAL(NPCNameLiteral.class, NPCNameLiteral::typeChecker),
    MOB_NAME(MobName.class, MobName::typeChecker),
    GATHERING(Gathering.class, Gathering::typeChecker),
    GATHERING_NODE(GatheringNode.class, GatheringNode::typeChecker),
    GATHERING_XP_GAIN(GatheringXPGain.class, GatheringXPGain::typeChecker),
    CRAFTING_STATION(CraftingStation.class, CraftingStation::typeChecker),
    MERCHANT_NAME(MerchantName.class, MerchantName::typeChecker),
    SYSTEM_NPC_NAME(SystemNPCName.class, SystemNPCName::typeChecker),
    SPECIAL_NPC_NAME(SpecialNPCName.class, SpecialNPCName::typeChecker),
    GUILD_BANNER(GuildBanner.class, GuildBanner::typeChecker),
    FAST_TRAVEL(FastTravel.class, FastTravel::typeChecker),
    MINI_QUEST_POST(MiniQuestPost.class, MiniQuestPost::typeChecker),
    LOOT_CHEST(LootChest.class, LootChest::typeChecker),
    CAVE_LOOT_CHEST(CaveLootChest.class, CaveLootChest::typeChecker),
    BOOTH_TITLE(BoothTitle.class, BoothTitle::typeChecker),
    BOOTH_SETUP(BoothSetup.class, BoothSetup::typeChecker),
    CRATE_REWARD(CrateReward.class, CrateReward::typeChecker),
    CRATE_OPENING(CrateOpening.class, CrateOpening::typeChecker),
    TIMER(Timer.class, Timer::typeChecker),
    TOTEM_HEALING(TotemHealing.class, TotemHealing::typeChecker),
    BOSS_ALTAR(BossAltar.class, BossAltar::typeChecker),
    DUNGEON_TITLE(DungeonTitle.class, DungeonTitle::typeChecker),
    RAID_TITLE(RaidTitle.class, RaidTitle::typeChecker),
    RAID_KEEPER(RaidKeeper.class, RaidKeeper::typeChecker),
    REWARD_CHEST(RewardChest.class, RewardChest::typeChecker),
    NO_TYPE(SimpleDisplay.class, null);

    private final Class<? extends WynnDisplayText> wdt;
    private final Predicate<Text> typeChecker;

    DisplayType(Class<? extends WynnDisplayText> wdt, Predicate<Text> typeChecker) {
        this.wdt = wdt;
        this.typeChecker = typeChecker;
    }

    private static DisplayType findType(Text text) {
        return Arrays.stream(values())
                .filter(displayType -> Objects.nonNull(displayType.typeChecker))
                .filter(displayType -> displayType.typeChecker.test(text))
                .findFirst().orElse(NO_TYPE);
    }

    public static Text findAndRun(Text text) {
        DisplayType find = findType(text);
        if(find == null) return text;
        try {
            return find.wdt.cast(find.wdt.getConstructor(Text.class).newInstance(text)).text();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            LogUtils.getLogger().warn("DisplayType.findAndRun has thrown exception.", e);
            debugClass.writeTextAsJSON(text, "DisplayEx");
            return text;
        }
    }
}
