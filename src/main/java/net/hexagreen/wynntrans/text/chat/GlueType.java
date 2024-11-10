package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.text.chat.types.glue.*;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public enum GlueType {
    QUEST_CLEAR(QuestGlue::new, QuestGlue::typeChecker),
    CAVE_CLEAR(CaveGlue::new, CaveGlue::typeChecker),
    OBJECTIVE_CLEAR(ObjectiveGlue::new, ObjectiveGlue::typeChecker),
    LEVEL_UP(LevelUpGlue::new, LevelUpGlue::typeChecker),
    CRATE_GET_PERSONAL(CrateGlue::new, CrateGlue::typeChecker),
    AREA_DISCOVERY(AreaDiscoveryGlue::new, AreaDiscoveryGlue::typeChecker),
    SECRET_DISCOVERY(SecretDiscoveryGlue::new, SecretDiscoveryGlue::typeChecker),
    DUNGEON_COMPLETE(DungeonCompleteGlue::new, DungeonCompleteGlue::typeChecker),
    REWARD_RECEIVED(RewardReceivedGlue::new, RewardReceivedGlue::typeChecker),
    DEATH_ITEM_LOST(DeathItemLostGlue::new, DeathItemLostGlue::typeChecker),
    WORLD_EVENT_FAILED(WorldEventFailedGlue::new, WorldEventFailedGlue::typeChecker),
    WORLD_EVENT_COMPLETE(WorldEventCompleteGlue::new, WorldEventCompleteGlue::typeChecker),
    PARTY_INVITED(PartyInvitedGlue::new, PartyInvitedGlue::typeChecker),
    STORE_PURCHASED(StorePurchasedGlue::new, StorePurchasedGlue::typeChecker),
    SYNDICATE_PROMOTION(SyndicatePromotionGlue::new, SyndicatePromotionGlue::typeChecker),

    NO_TYPE(null, null);

    private final Supplier<TextGlue> glue;
    private final Predicate<Text> typeChecker;

    public static GlueType findType(Text text) {
        return Arrays.stream(GlueType.values())
                .filter(glueType -> Objects.nonNull(glueType.typeChecker))
                .filter(glueType -> glueType.typeChecker.test(text))
                .findFirst().orElse(NO_TYPE);
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
