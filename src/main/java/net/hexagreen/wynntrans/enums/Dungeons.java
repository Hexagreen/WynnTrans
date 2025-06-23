package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public enum Dungeons {
    DECREPIT_SEWERS(Text.translatable("wytr.dungeon.decrepitSewers"),
            Text.translatable("wytr.dungeon.decrepitSewers.bossReward"),
            Text.translatable("wytr.dungeon.decrepitSewers.fragment")),
    INFESTED_PIT(Text.translatable("wytr.dungeon.infestedPit"),
            Text.translatable("wytr.dungeon.infestedPit.bossReward"),
            Text.translatable("wytr.dungeon.infestedPit.fragment")),
    UNDERWORLD_CRYPT(Text.translatable("wytr.dungeon.underworldCrypt"),
            Text.translatable("wytr.dungeon.underworldCrypt.bossReward"),
            Text.translatable("wytr.dungeon.underworldCrypt.fragment")),
    LOST_SANCTUARY(Text.translatable("wytr.dungeon.lostSanctuary"),
            Text.translatable("wytr.dungeon.lostSanctuary.bossReward"),
            Text.translatable("wytr.dungeon.lostSanctuary.fragment")),
    TIMELOST_SANCTUM(Text.translatable("wytr.dungeon.timelostSanctum"),
            Text.translatable("wytr.dungeon.timelostSanctum.bossReward"),
            Text.translatable("wytr.dungeon.timelostSanctum.fragment")),
    SAND_SWEPT_TOMB(Text.translatable("wytr.dungeon.sandSweptTomb"),
            Text.translatable("wytr.dungeon.sandSweptTomb.bossReward"),
            Text.translatable("wytr.dungeon.sandSweptTomb.fragment")),
    ICE_BARROWS(Text.translatable("wytr.dungeon.iceBarrows"),
            Text.translatable("wytr.dungeon.iceBarrows.bossReward"),
            Text.translatable("wytr.dungeon.iceBarrows.fragment")),
    UNDERGROWTH_RUINS(Text.translatable("wytr.dungeon.undergrowthRuins"),
            Text.translatable("wytr.dungeon.undergrowthRuins.bossReward"),
            Text.translatable("wytr.dungeon.undergrowthRuins.fragment")),
    GALLEONS_GRAVEYARD(Text.translatable("wytr.dungeon.galleonsGraveyard"),
            Text.translatable("wytr.dungeon.galleonsGraveyard.bossReward"),
            Text.translatable("wytr.dungeon.galleonsGraveyard.fragment")),
    FALLEN_FACTORY(Text.translatable("wytr.dungeon.fallenFactory"),
            Text.translatable("wytr.dungeon.fallenFactory.bossReward"),
            Text.translatable("wytr.dungeon.fallenFactory.fragment")),
    ELDRITCH_OUTLOOK(Text.translatable("wytr.dungeon.eldritchOutlook"),
            Text.translatable("wytr.dungeon.eldritchOutlook.bossReward"),
            Text.translatable("wytr.dungeon.eldritchOutlook.fragment")),
    UNKNOWN(Text.literal("Unknown"),
            Text.literal("Unknown"),
            Text.literal("Unknown"));

    private final Text dungeonName;
    private final Text dungeonBossReward;
    private final Text dungeonFragment;
    private boolean isCorrupted = false;

    public static Dungeons getDungeons(String dungeonOrRewardName) {
        boolean isCorrupted = dungeonOrRewardName.contains("Corrupted") || dungeonOrRewardName.contains("corrupted");
        String stripped = dungeonOrRewardName.replaceFirst("[Cc]orrupted ?", "")
                .replace("À", "").replace("'", "").replace("-", "")
                .toLowerCase();
        Dungeons dungeons = match(stripped);
        dungeons.setCorrupted(isCorrupted);
        return dungeons;
    }

    private static Dungeons match(String name) {
        if(name.contains("decrepit") || name.equals("witherheads rib")) return DECREPIT_SEWERS;
        if(name.contains("infested") || name.equals("arakadicus eye")) return INFESTED_PIT;
        if(name.contains("underworld") || name.equals("charons brain")) return UNDERWORLD_CRYPT;
        if(name.contains("sanctuary") || name.equals("garoths breath")) return LOST_SANCTUARY;
        if(name.contains("sanctum") || name.equals("garoths writings")) return TIMELOST_SANCTUM;
        if(name.contains("sand") || name.equals("hashrs bone")) return SAND_SWEPT_TOMB;
        if(name.contains("barrows") || name.equals("theoricks ice shard")) return ICE_BARROWS;
        if(name.contains("undergrowth") || name.equals("slykaars heart")) return UNDERGROWTH_RUINS;
        if(name.contains("galleons") || name.equals("redbeards hair")) return GALLEONS_GRAVEYARD;
        if(name.contains("fallen") || name.equals("antikythera supercomputers chip")) return FALLEN_FACTORY;
        if(name.contains("eldritch") || name.equals("the eyes pupil")) return ELDRITCH_OUTLOOK;
        else return UNKNOWN;
    }

    Dungeons(Text dungeonName, Text dungeonBossReward, Text dungeonFragment) {
        this.dungeonName = dungeonName;
        this.dungeonBossReward = dungeonBossReward;
        this.dungeonFragment = dungeonFragment;
    }

    public MutableText getDungeonName() {
        MutableText out = Text.empty();
        if(this.isCorrupted) return out.append(Text.translatable("wytr.dungeon.corrupted", dungeonName));
        return out.append(dungeonName);
    }

    public Text getDungeonBossReward() {
        MutableText out = Text.empty();
        if(this.isCorrupted) return out.append(Text.translatable("wytr.dungeon.corrupted", dungeonBossReward));
        return out.append(dungeonBossReward);
    }

    public Text getDungeonFragment() {
        MutableText out = Text.empty();
        if(this.isCorrupted) return out.append(Text.translatable("wytr.dungeon.corrupted", dungeonFragment));
        return out.append(dungeonFragment);
    }

    private void setCorrupted(boolean corrupted) {
        this.isCorrupted = corrupted;
    }

}
