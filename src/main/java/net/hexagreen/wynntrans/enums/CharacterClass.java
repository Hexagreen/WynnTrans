package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Locale;

public enum CharacterClass {
    ARCHER(Text.translatable("wytr.classes.archer"), Text.translatable("wytr.classes.archer.weapon")),
    WARRIOR(Text.translatable("wytr.classes.warrior"), Text.translatable("wytr.classes.warrior.weapon")),
    MAGE(Text.translatable("wytr.classes.mage"), Text.translatable("wytr.classes.mage.weapon")),
    ASSASSIN(Text.translatable("wytr.classes.assassin"), Text.translatable("wytr.classes.assassin.weapon")),
    SHAMAN(Text.translatable("wytr.classes.shaman"), Text.translatable("wytr.classes.shaman.weapon")),
    HUNTER(Text.translatable("wytr.classes.archer_alt"), Text.translatable("wytr.classes.archer.weapon")),
    KNIGHT(Text.translatable("wytr.classes.warrior_alt"), Text.translatable("wytr.classes.warrior.weapon")),
    DARK_WIZARD(Text.translatable("wytr.classes.mage_alt"), Text.translatable("wytr.classes.mage.weapon")),
    NINJA(Text.translatable("wytr.classes.assassin_alt"), Text.translatable("wytr.classes.assassin.weapon")),
    SKYSEER(Text.translatable("wytr.classes.shaman_alt"), Text.translatable("wytr.classes.shaman.weapon")),
    ;

    private final MutableText className;
    private final MutableText weapon;

    CharacterClass(MutableText className, MutableText weapon) {
        this.className = className;
        this.weapon = weapon;
    }

    public static MutableText getClassName(String className) {
        return valueOf(className.toUpperCase(Locale.ENGLISH).replace(" ", "_")).className;
    }

    public static MutableText getWeapon(String className) {
        return valueOf(className.toUpperCase(Locale.ENGLISH).replace(" ", "_")).weapon;
    }
}
