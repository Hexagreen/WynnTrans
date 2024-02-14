package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.TranslatableTextContent;

import java.util.Locale;

public enum CharacterClass {
    ARCHER(MutableText.of(new TranslatableTextContent("wytr.classes.archer", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    WARRIOR(MutableText.of(new TranslatableTextContent("wytr.classes.warrior", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    MAGE(MutableText.of(new TranslatableTextContent("wytr.classes.mage", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    ASSASSIN(MutableText.of(new TranslatableTextContent("wytr.classes.assassin", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    SHAMAN(MutableText.of(new TranslatableTextContent("wytr.classes.shaman", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    HUNTER(MutableText.of(new TranslatableTextContent("wytr.classes.archer_alt", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    KNIGHT(MutableText.of(new TranslatableTextContent("wytr.classes.warrior_alt", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    DARK_WIZARD(MutableText.of(new TranslatableTextContent("wytr.classes.mage_alt", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    NINJA(MutableText.of(new TranslatableTextContent("wytr.classes.assassin_alt", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    SKYSEER(MutableText.of(new TranslatableTextContent("wytr.classes.shaman_alt", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    ;

    private final MutableText className;

    CharacterClass(MutableText className) {
        this.className = className;
    }

    public static MutableText getClassName(String className) {
        return valueOf(className.toUpperCase(Locale.ENGLISH).replace(" ", "_")).className;
    }
}
