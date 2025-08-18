package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

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
                .replace("À", "").replace("'", "")
                .replace("-", "").replace(" ", "")
                .toLowerCase();
        Dungeons dungeons = match(stripped);
        dungeons.setCorrupted(isCorrupted);
        return dungeons;
    }

    private static Dungeons match(String name) {
        return switch(name) {
            case "decrepitsewers", "witherheadsrib" -> DECREPIT_SEWERS;
            case "infestedpit", "arakadicuseye" -> INFESTED_PIT;
            case "underworldcrypt", "charonssoul" -> UNDERWORLD_CRYPT;
            case "lostsanctuary", "garothsbreath" -> LOST_SANCTUARY;
            case "timelostsanctum", "garothswritings" -> TIMELOST_SANCTUM;
            case "sandswepttomb", "hashrscrown" -> SAND_SWEPT_TOMB;
            case "icebarrows", "theorickspermafrost" -> ICE_BARROWS;
            case "undergrowthruins", "slykaarsheart" -> UNDERGROWTH_RUINS;
            case "galleonsgraveyard", "redbeardshair" -> GALLEONS_GRAVEYARD;
            case "fallenfactory", "antikytheraschip" -> FALLEN_FACTORY;
            case "eldritchoutlook", "theeyespupil" -> ELDRITCH_OUTLOOK;
            default -> UNKNOWN;
        };
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

    public MutableText getDungeonBossRewardLore() {
        if(this == UNKNOWN) return Text.empty();
        String key = ((TranslatableTextContent) dungeonBossReward.getContent()).getKey();
        key = this.isCorrupted ? key + ".desc_c" : key + ".desc_n";
        return Text.translatable(key);
    }

    private void setCorrupted(boolean corrupted) {
        this.isCorrupted = corrupted;
    }

}
