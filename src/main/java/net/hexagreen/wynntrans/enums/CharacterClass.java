package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Locale;

public enum CharacterClass {
    ARCHER(Text.translatable("wytr.class.archer"), Text.translatable("wytr.class.archer.weapon")),
    WARRIOR(Text.translatable("wytr.class.warrior"), Text.translatable("wytr.class.warrior.weapon")),
    MAGE(Text.translatable("wytr.class.mage"), Text.translatable("wytr.class.mage.weapon")),
    ASSASSIN(Text.translatable("wytr.class.assassin"), Text.translatable("wytr.class.assassin.weapon")),
    SHAMAN(Text.translatable("wytr.class.shaman"), Text.translatable("wytr.class.shaman.weapon")),
    HUNTER(Text.translatable("wytr.class.archer_alt"), Text.translatable("wytr.class.archer.weapon")),
    KNIGHT(Text.translatable("wytr.class.warrior_alt"), Text.translatable("wytr.class.warrior.weapon")),
    DARK_WIZARD(Text.translatable("wytr.class.mage_alt"), Text.translatable("wytr.class.mage.weapon")),
    NINJA(Text.translatable("wytr.class.assassin_alt"), Text.translatable("wytr.class.assassin.weapon")),
    SKYSEER(Text.translatable("wytr.class.shaman_alt"), Text.translatable("wytr.class.shaman.weapon"));

    private final MutableText className;
    private final MutableText weapon;

    public static MutableText getClassName(String className) {
        return valueOf(className.toUpperCase(Locale.ENGLISH).replace(" ", "_")).className;
    }

    public static MutableText getWeapon(String className) {
        return valueOf(className.toUpperCase(Locale.ENGLISH).replace(" ", "_")).weapon;
    }

    CharacterClass(MutableText className, MutableText weapon) {
        this.className = className;
        this.weapon = weapon;
    }
}
