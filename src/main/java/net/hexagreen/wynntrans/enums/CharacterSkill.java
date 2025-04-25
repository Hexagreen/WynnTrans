package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;

public enum CharacterSkill {
    STR(Text.translatable("wytr.skill.strength").setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN))),
    DEX(Text.translatable("wytr.skill.dexterity").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))),
    INT(Text.translatable("wytr.skill.intelligence").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
    DEF(Text.translatable("wytr.skill.defence").setStyle(Style.EMPTY.withColor(Formatting.RED))),
    AGI(Text.translatable("wytr.skill.agility").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

    private final Text stat;

    public static MutableText getSkill(String name) {
        switch(name.toLowerCase(Locale.ENGLISH)) {
            case "strength" -> {
                return STR.stat.copy();
            }
            case "dexterity" -> {
                return DEX.stat.copy();
            }
            case "intelligence" -> {
                return INT.stat.copy();
            }
            case "defence" -> {
                return DEF.stat.copy();
            }
            case "agility" -> {
                return AGI.stat.copy();
            }
        }
        return Text.literal("UNKNOWN");
    }

    CharacterSkill(Text stat) {
        this.stat = stat;
    }
}
