package net.hexagreen.wynntrans.enums;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public enum CharacterSkill {
    STR(Text.translatable("wytr.skill.strength").setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN))),
    DEX(Text.translatable("wytr.skill.dexterity").setStyle(Style.EMPTY.withColor(Formatting.YELLOW))),
    INT(Text.translatable("wytr.skill.intelligence").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
    DEF(Text.translatable("wytr.skill.defence").setStyle(Style.EMPTY.withColor(Formatting.RED))),
    AGI(Text.translatable("wytr.skill.agility").setStyle(Style.EMPTY.withColor(Formatting.WHITE)));

    private final Text stat;

    public static Text getSkill(String name) {
        switch(name) {
            case "strength" -> {
                return STR.stat;
            }
            case "dexterity" -> {
                return DEX.stat;
            }
            case "intelligence" -> {
                return INT.stat;
            }
            case "defence" -> {
                return DEF.stat;
            }
            case "agility" -> {
                return AGI.stat;
            }
        }
        return null;
    }

    CharacterSkill(Text stat) {
        this.stat = stat;
    }
}
