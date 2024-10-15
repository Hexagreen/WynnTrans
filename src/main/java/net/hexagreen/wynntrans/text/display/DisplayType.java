package net.hexagreen.wynntrans.text.display;

import net.hexagreen.wynntrans.text.display.types.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DisplayType {
    DAMAGE_INDICATOR(DamageIndicator::new, DamageIndicator::typeChecker),
    COMBAT_XP_GAIN(CombatXPGain::new, CombatXPGain::typeChecker),
    CAVE_PROGRESS(CaveProgress::new, CaveProgress::typeChecker),
    CAVE_QUALITY(CaveQuality::new, CaveQuality::typeChecker),
    CAVE_TITLE(CaveTitle::new, CaveTitle::typeChecker),
    CAVE_REWARD(CaveReward::new, CaveReward::typeChecker),
    NPC_NAME_LITERAL(NPCNameLiteral::new, NPCNameLiteral::typeChecker),
    MOB_NAME(MobName::new, MobName::typeChecker),
    GATHERING(Gathering::new, Gathering::typeChecker),
    GATHERING_NODE(GatheringNode::new, GatheringNode::typeChecker),
    GATHERING_XP_GAIN(GatheringXPGain::new, GatheringXPGain::typeChecker),
    CRAFTING_STATION(CraftingStation::new, CraftingStation::typeChecker),
    MERCHANT_NAME(MerchantName::new, MerchantName::typeChecker),
    SYSTEM_NPC_NAME(SystemNPCName::new, SystemNPCName::typeChecker),
    SPECIAL_NPC_NAME(SpecialNPCName::new, SpecialNPCName::typeChecker),
    GUILD_BANNER(GuildBanner::new, GuildBanner::typeChecker),
    FAST_TRAVEL(FastTravel::new, FastTravel::typeChecker),
    MINI_QUEST_POST(MiniQuestPost::new, MiniQuestPost::typeChecker),
    LOOT_CHEST(LootChest::new, LootChest::typeChecker),
    CAVE_LOOT_CHEST(CaveLootChest::new, CaveLootChest::typeChecker),
    BOOTH_TITLE(BoothTitle::new, BoothTitle::typeChecker),
    BOOTH_SETUP(BoothSetup::new, BoothSetup::typeChecker),
    CRATE_REWARD(CrateReward::new, CrateReward::typeChecker),
    CRATE_OPENING(CrateOpening::new, CrateOpening::typeChecker),
    TIMER(Timer::new, Timer::typeChecker),
    TOTEM_HEALING(TotemHealing::new, TotemHealing::typeChecker),
    BOSS_ALTAR(BossAltar::new, BossAltar::typeChecker),
    KILL_COUNTER(SlayCounter::new, SlayCounter::typeChecker),
    DUNGEON_TITLE(DungeonTitle::new, DungeonTitle::typeChecker),
    RAID_TITLE(RaidTitle::new, RaidTitle::typeChecker),
    RAID_KEEPER(RaidKeeper::new, RaidKeeper::typeChecker),
    REWARD_CHEST(RewardChest::new, RewardChest::typeChecker),
    WORLD_EVENT_NAME(WorldEventName::new, WorldEventName::typeChecker),
    WORLD_EVENT_TIMER(WorldEventTimer::new, WorldEventTimer::typeChecker),
    WORLD_EVENT_BANNER(WorldEventBanner::new, WorldEventBanner::typeChecker),
    SUMMONS_NAME(SummonsName::new, SummonsName::typeChecker),
    PET_NAME(PetName::new, PetName::typeChecker),

    SPIRITS_WEEKLY_BOON(ProxySimpleDisplay::new, ProxySimpleDisplay.Templates.DRAWING_OF_THE_SPIRITS_TIMER.getTypeChecker()),

    NO_TYPE(SimpleDisplay::new, null);

    private final Function<Text, WynnDisplayText> wdt;
    private final Predicate<Text> typeChecker;

    private static DisplayType findType(Text text) {
        return Arrays.stream(values())
                .filter(displayType -> Objects.nonNull(displayType.typeChecker))
                .filter(displayType -> displayType.typeChecker.test(text))
                .findFirst().orElse(NO_TYPE);
    }

    public static Text findAndRun(Text text) {
        DisplayType find = findType(text);
        if(find == null) return text;
        return find.wdt.apply(text).text();
    }

    DisplayType(Function<Text, WynnDisplayText> wdt, Predicate<Text> typeChecker) {
        this.wdt = wdt;
        this.typeChecker = typeChecker;
    }
}
