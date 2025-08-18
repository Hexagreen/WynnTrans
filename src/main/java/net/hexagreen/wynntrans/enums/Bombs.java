package net.hexagreen.wynntrans.enums;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum Bombs {
    COMBAT_XP(Text.translatable("wytr.bomb.name.combat"),
            Text.translatable("wytr.bomb.desc.combat"),
            Text.translatable("wytr.bomb.time.combat")),
    PROFESSION_XP(Text.translatable("wytr.bomb.name.profXp"),
            Text.translatable("wytr.bomb.desc.profXp"),
            Text.translatable("wytr.bomb.time.profXp")),
    PROFESSION_SPEED(Text.translatable("wytr.bomb.name.profSpeed"),
            Text.translatable("wytr.bomb.desc.profSpeed"),
            Text.translatable("wytr.bomb.time.profSpeed")),
    ITEM(Text.translatable("wytr.bomb.name.item"),
            Text.translatable("wytr.bomb.desc.item"),
            Text.translatable("wytr.bomb.time.item").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/claimitembomb"))
                    .withUnderline(true))),
    SCROLL_CHARGE(Text.translatable("wytr.bomb.name.scrollCharge"),
            Text.translatable("wytr.bomb.desc.scrollCharge"),
            Text.translatable("wytr.bomb.time.scrollCharge")),
    DUNGEON(Text.translatable("wytr.bomb.name.dungeon"),
            Text.translatable("wytr.bomb.desc.dungeon"),
            Text.translatable("wytr.bomb.time.dungeon")),
    LOOT(Text.translatable("wytr.bomb.name.loot"),
            Text.translatable("wytr.bomb.desc.loot"),
            Text.translatable("wytr.bomb.time.loot")),
    LOOT_CHEST(Text.translatable("wytr.bomb.name.lootChest"),
            Text.translatable("wytr.bomb.desc.lootChest"),
            Text.translatable("wytr.bomb.time.lootChest")),
    INGREDIENT(Text.translatable("wytr.bomb.name.ingredient"),
            Text.translatable("wytr.bomb.desc.ingredient"),
            Text.translatable("wytr.bomb.time.ingredient").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/claimingredientbomb"))
                    .withUnderline(true))),
    PARTY(Text.translatable("wytr.bomb.name.party"),
            Text.translatable("wytr.bomb.desc.party"),
            Text.translatable("wytr.bomb.time.party"));

    private final Text bombName;
    private final Text bombDescription;
    private final Text bombTime;

    public static Bombs findBomb(String bombName) {
        if(bombName.contains("Combat Experience Bomb")) {
            return COMBAT_XP;
        }
        else if(bombName.contains("Profession XP Bomb")) {
            return PROFESSION_XP;
        }
        else if(bombName.contains("Profession Speed Bomb")) {
            return PROFESSION_SPEED;
        }
        else if(bombName.contains("Item Bomb")) {
            return ITEM;
        }
        else if(bombName.contains("Scroll Charge Bomb")) {
            return SCROLL_CHARGE;
        }
        else if(bombName.contains("Dungeon Bomb")) {
            return DUNGEON;
        }
        else if(bombName.contains("Loot Chest Bomb")) {
            return LOOT_CHEST;
        }
        else if(bombName.contains("Loot Bomb")) {
            return LOOT;
        }
        else if(bombName.contains("Ingredient Bomb")) {
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

    public MutableText getName() {
        return bombName.copy();
    }

    public Text getDesc() {
        return bombDescription.copy().withColor(0xFEDF71);
    }

    public Text getTime() {
        return bombTime.copy().withColor(0xFEDF71);
    }
}
