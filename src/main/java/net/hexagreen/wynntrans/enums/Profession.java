package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

import java.util.Arrays;

public enum Profession {
    COOKING(Text.literal("§fⒶ"), "cooking"),
    MINING(Text.literal("Ⓑ"), "mining", MutableText.of(new TranslatableTextContent("wytr.profession.mining.tool", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    WOODCUTTING(Text.literal("Ⓒ"), "woodcutting", MutableText.of(new TranslatableTextContent("wytr.profession.woodcutting.tool", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    JEWELING(Text.literal("§fⒹ"), "jeweling"),
    SCRIBING(Text.literal("§fⒺ"), "scribing"),
    TAILORING(Text.literal("§fⒻ"), "tailoring"),
    WEAPONSMITHING(Text.literal("§fⒼ"), "weaponsmithing"),
    ARMOURING(Text.literal("§fⒽ"), "armouring"),
    WOODWORKING(Text.literal("§fⒾ"), "woodworking"),
    FARMING(Text.literal("Ⓙ"), "farming", MutableText.of(new TranslatableTextContent("wytr.profession.farming.tool", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    FISHING(Text.literal("Ⓚ"), "fishing", MutableText.of(new TranslatableTextContent("wytr.profession.fishing.tool", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    ALCHEMISM(Text.literal("§fⓁ"), "alchemism");

    private final MutableText text;
    private final String key;
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
        return Arrays.stream(values()).filter(profession -> profession.key.equals(profName.toLowerCase())).findFirst().orElse(null);
    }

    Profession(MutableText text, String key) {
        this.text = text;
        this.key = key;
        this.tool = null;
    }

    Profession(MutableText text, String key, MutableText tool) {
        this.text = text;
        this.key = key;
        this.tool = tool;
    }

    public MutableText getTextWithIcon() {
        MutableText result = this.text.copy().append(" ");
        return result.append(Text.translatable("wytr.profession." + this.key));
    }

    public MutableText getIcon() {
        return this.text.copy();
    }

    public String getKey() {
        return this.key;
    }

    public MutableText getText() {
        return Text.translatable("wytr.profession." + this.key);
    }

    public MutableText getTool(char tier) {
        if(this.tool != null) {
            MutableText result = this.tool.copy();
            return result.append(" T" + tier);
        }
        return Text.empty();
    }
}
