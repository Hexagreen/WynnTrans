package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;

public enum CharacterSkill {
    STR(Text.translatable("wytr.skill.strength").setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN)), Elements.EARTH),
    DEX(Text.translatable("wytr.skill.dexterity").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)), Elements.THUNDER),
    INT(Text.translatable("wytr.skill.intelligence").setStyle(Style.EMPTY.withColor(Formatting.AQUA)), Elements.WATER),
    DEF(Text.translatable("wytr.skill.defence").setStyle(Style.EMPTY.withColor(Formatting.RED)), Elements.FIRE),
    AGI(Text.translatable("wytr.skill.agility").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), Elements.AIR),
    UNKNOWN(Text.literal("UNKNOWN"), null);

    private final Text stat;
    private final Elements relativeElement;

    public static CharacterSkill getSkill(String name) {
        switch(name.trim().toLowerCase(Locale.ENGLISH)) {
            case "strength" -> {
                return STR;
            }
            case "dexterity" -> {
                return DEX;
            }
            case "intelligence" -> {
                return INT;
            }
            case "defence" -> {
                return DEF;
            }
            case "agility" -> {
                return AGI;
            }
        }
        return UNKNOWN;
    }

    CharacterSkill(Text stat, Elements relativeElement) {
        this.stat = stat;
        this.relativeElement = relativeElement;
    }

    public MutableText getStat() {
        return stat.copy();
    }

    public MutableText getIcon() {
        return relativeElement.getIcon();
    }

    public MutableText getStatWithIcon() {
        return getIcon().append(" ").append(stat.copy());
    }

    public Elements getRelativeElement() {
        return relativeElement;
    }
}
