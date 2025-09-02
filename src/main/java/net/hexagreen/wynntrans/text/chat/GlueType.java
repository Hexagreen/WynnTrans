package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.text.chat.types.glue.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum GlueType {
    QUEST_CLEAR(QuestGlue::new, QuestGlue::typeChecker),
    CAVE_CLEAR(CaveGlue::new, CaveGlue::typeChecker),
    OBJECTIVE_CLEAR(ObjectiveGlue::new, ObjectiveGlue::typeChecker),
    LEVEL_UP(LevelUpGlue::new, LevelUpGlue::typeChecker),
    AREA_DISCOVERY(AreaDiscoveryGlue::new, AreaDiscoveryGlue::typeChecker),
    SECRET_DISCOVERY(SecretDiscoveryGlue::new, SecretDiscoveryGlue::typeChecker),
    DUNGEON_COMPLETE(DungeonCompleteGlue::new, DungeonCompleteGlue::typeChecker),
    REWARD_RECEIVED(RewardReceivedGlue::new, RewardReceivedGlue::typeChecker),
    DEATH_ITEM_LOST(DeathItemLostGlue::new, DeathItemLostGlue::typeChecker),
    WORLD_EVENT_FAILED(WorldEventFailedGlue::new, WorldEventFailedGlue::typeChecker),
    WORLD_EVENT_COMPLETE(WorldEventCompleteGlue::new, WorldEventCompleteGlue::typeChecker),
    PARTY_INVITED(PartyInvitedGlue::new, PartyInvitedGlue::typeChecker),
    BOMB_START(BombStartGlue::new, BombStartGlue::typeChecker),
    STORE_PURCHASED(StorePurchasedGlue::new, StorePurchasedGlue::typeChecker),
    SYNDICATE_PROMOTION(SyndicatePromotionGlue::new, SyndicatePromotionGlue::typeChecker),
    LOOTRUN_BEACON(LootrunBeaconGlue::new, LootrunBeaconGlue::typeChecker),
    LOOTRUN_CHALLENGE_COMPLETE(LootrunChallengeCompleteGlue::new, LootrunChallengeCompleteGlue::typeChecker),
    LOOTRUN_CHALLENGE_FAIL(LootrunChallengeFailGlue::new, LootrunChallengeFailGlue::typeChecker),
    LOOTRUN_MISSION_COMPLETE(LootrunMissionCompleteGlue::new, LootrunMissionCompleteGlue::typeChecker),
    LOOTRUN_SUMMARY(LootrunSummaryGlue::new, LootrunSummaryGlue::typeChecker),
    LOOTRUN_FAILED(LootrunFailedGlue::new, LootrunFailedGlue::typeChecker),
    HOUSING_ENTER(HousingEnterGlue::new, HousingEnterGlue::typeChecker),

    NO_TYPE(null, null);

    private static final List<GlueType> CACHED_CHECKER =
            Arrays.stream(GlueType.values())
                    .filter(e -> e.typeChecker != null)
                    .toList();
    private final Supplier<TextGlue> glue;
    private final Predicate<Text> typeChecker;

    public static GlueType findType(Text text) {
        for(GlueType c : CACHED_CHECKER) {
            if(c.typeChecker.test(text)) return c;
        }
        return NO_TYPE;
    }

    public static TextGlue findAndGet(Text text) {
        GlueType find = findType(text);
        return find == NO_TYPE ? null : find.glue.get();
    }

    GlueType(Supplier<TextGlue> glue, Predicate<Text> typeChecker) {
        this.glue = glue;
        this.typeChecker = typeChecker;
    }
}
