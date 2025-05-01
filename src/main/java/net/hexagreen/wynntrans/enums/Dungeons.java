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

    private static final Text corrupted = Text.translatable("wytr.dungeon.corrupted");
    private final Text dungeonName;
    private final Text dungeonBossReward;
    private final Text dungeonFragment;
    private boolean isCorrupted = false;

    public static Dungeons getDungeons(String dungeonName) {
        boolean isCorrupted = dungeonName.contains("Corrupted") || dungeonName.contains("corrupted");
        String stripped = dungeonName.replaceFirst("[Cc]orrupted", "")
                .replace("À", "").replace("'", "").replace("-", "")
                .toLowerCase();
        Dungeons dungeons = match(stripped);
        dungeons.setCorrupted(isCorrupted);
        return dungeons;
    }

    private static Dungeons match(String name) {
        if(name.contains("decrepit")) return DECREPIT_SEWERS;
        if(name.contains("infested")) return INFESTED_PIT;
        if(name.contains("underworld")) return UNDERWORLD_CRYPT;
        if(name.contains("sanctuary")) return LOST_SANCTUARY;
        if(name.contains("sanctum")) return TIMELOST_SANCTUM;
        if(name.contains("sand")) return SAND_SWEPT_TOMB;
        if(name.contains("barrows")) return ICE_BARROWS;
        if(name.contains("undergrowth")) return UNDERGROWTH_RUINS;
        if(name.contains("galleons")) return GALLEONS_GRAVEYARD;
        if(name.contains("fallen")) return FALLEN_FACTORY;
        if(name.contains("eldritch")) return ELDRITCH_OUTLOOK;
        else return UNKNOWN;
    }

    Dungeons(Text dungeonName, Text dungeonBossReward, Text dungeonFragment) {
        this.dungeonName = dungeonName;
        this.dungeonBossReward = dungeonBossReward;
        this.dungeonFragment = dungeonFragment;
    }

    public MutableText getDungeonName() {
        MutableText out = Text.empty();
        if(this.isCorrupted) return out.append(corrupted).append(dungeonName);
        return out.append(dungeonName);
    }

    public Text getDungeonBossReward() {
        MutableText out = Text.empty();
        if(this.isCorrupted) return out.append(corrupted).append(dungeonBossReward);
        return out.append(dungeonBossReward);
    }

    public Text getDungeonFragment() {
        MutableText out = Text.empty();
        if(this.isCorrupted) return out.append(corrupted).append(dungeonFragment);
        return out.append(dungeonFragment);
    }

    private void setCorrupted(boolean corrupted) {
        this.isCorrupted = corrupted;
    }

}
