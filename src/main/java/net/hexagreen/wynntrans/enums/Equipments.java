package net.hexagreen.wynntrans.enums;

import net.minecraft.text.Text;

import java.util.Locale;

public enum Equipments {
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
    RELIK(CharacterClass.SHAMAN.getWeapon());

    private final Text text;

    Equipments(Text text) {
        this.text = text;
    }

    public Text getText() {
        return this.text.copy();
    }

    public static Text getText(String equip) {
        return valueOf(equip.toUpperCase(Locale.ENGLISH)).text.copy();
    }
}
