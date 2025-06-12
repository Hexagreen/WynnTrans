package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Locale;

public enum EquipmentAndConsumables {
    HELMET(Text.translatable("wytr.equipment.helmet")),
    CHESTPLATE(Text.translatable("wytr.equipment.chestplate")),
    LEGGINGS(Text.translatable("wytr.equipment.leggings")),
    BOOTS(Text.translatable("wytr.equipment.boots")),
    RING(Text.translatable("wytr.equipment.ring")),
    NECKLACE(Text.translatable("wytr.equipment.necklace")),
    BRACELET(Text.translatable("wytr.equipment.bracelet")),
    SPEAR(CharacterClass.WARRIOR.getWeapon()),
    BOW(CharacterClass.ARCHER.getWeapon()),
    WAND(CharacterClass.MAGE.getWeapon()),
    DAGGER(CharacterClass.ASSASSIN.getWeapon()),
    RELIK(CharacterClass.SHAMAN.getWeapon()),
    POTION(Text.translatable("wytr.consumable.potion")),
    SCROLL(Text.translatable("wytr.consumable.scroll")),
    FOOD(Text.translatable("wytr.consumable.food"));

    private final Text text;

    public static MutableText getText(String equip) {
        return valueOf(equip.toUpperCase(Locale.ENGLISH)).text.copy();
    }

    EquipmentAndConsumables(Text text) {
        this.text = text;
    }

    public MutableText getText() {
        return this.text.copy();
    }
}
