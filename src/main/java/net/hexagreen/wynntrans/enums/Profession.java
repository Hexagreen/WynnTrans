package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Locale;

public enum Profession {
    COOKING(Text.literal("§fⒶ"), Text.translatable("wytr.profession.cooking")),
    MINING(Text.literal("Ⓑ"), Text.translatable("wytr.profession.mining"), Text.translatable("wytr.profession.mining.tool")),
    WOODCUTTING(Text.literal("Ⓒ"), Text.translatable("wytr.profession.woodcutting"), Text.translatable("wytr.profession.woodcutting.tool")),
    JEWELING(Text.literal("§fⒹ"), Text.translatable("wytr.profession.jeweling")),
    SCRIBING(Text.literal("§fⒺ"), Text.translatable("wytr.profession.scribing")),
    TAILORING(Text.literal("§fⒻ"), Text.translatable("wytr.profession.tailoring")),
    WEAPONSMITHING(Text.literal("§fⒼ"), Text.translatable("wytr.profession.weaponsmithing")),
    ARMOURING(Text.literal("§fⒽ"), Text.translatable("wytr.profession.armouring")),
    WOODWORKING(Text.literal("§fⒾ"), Text.translatable("wytr.profession.woodworking")),
    FARMING(Text.literal("Ⓙ"), Text.translatable("wytr.profession.farming"), Text.translatable("wytr.profession.farming.tool")),
    FISHING(Text.literal("Ⓚ"), Text.translatable("wytr.profession.fishing"), Text.translatable("wytr.profession.fishing.tool")),
    ALCHEMISM(Text.literal("§fⓁ"), Text.translatable("wytr.profession.alchemism"));

    private final MutableText icon;
    private final MutableText text;
    private final MutableText tool;

    public static Profession getProfession(char icon) {
        int index = icon - 'Ⓐ';
        if(index >= 12) {
            switch(icon - 57344) {
                case 0 -> {
                    return FARMING;
                }
                case 1 -> {
                    return FISHING;
                }
                case 2 -> {
                    return MINING;
                }
                case 3 -> {
                    return WOODCUTTING;
                }
            }
        }
        return values()[index];
    }

    public static Profession getProfession(String profName) {
        return Arrays.stream(values())
                .filter(profession -> profession.toString().equals(profName.toUpperCase(Locale.ENGLISH)))
                .findFirst()
                .orElse(null);
    }

    Profession(MutableText icon, MutableText text) {
        this.icon = icon;
        this.text = text;
        this.tool = null;
    }

    Profession(MutableText icon, MutableText text, MutableText tool) {
        this.icon = icon;
        this.text = text;
        this.tool = tool;
    }

    public MutableText getTextWithIcon() {
        MutableText result = this.icon.copy().append(" ");
        return result.append(this.text.copy());
    }

    public MutableText getIcon() {
        return this.icon.copy();
    }

    public String getKey() {
        return this.toString().toLowerCase(Locale.ENGLISH);
    }

    public MutableText getText() {
        return this.text.copy();
    }

    public MutableText getTool(String tier) {
        if(this.tool != null) {
            MutableText result = this.tool.copy();
            return result.append(" T" + tier);
        }
        return Text.empty();
    }
}
