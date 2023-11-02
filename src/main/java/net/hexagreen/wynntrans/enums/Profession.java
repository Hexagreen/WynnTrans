package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

public enum Profession {
    COOKING(Text.literal("Ⓐ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "cooking"),
    MINING(Text.literal("Ⓑ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "mining", MutableText.of(new TranslatableTextContent("wytr.profession.mining.tool", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    WOODCUTTING(Text.literal("Ⓒ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "woodcutting", MutableText.of(new TranslatableTextContent("wytr.profession.woodcutting.tool", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    JEWELING(Text.literal("Ⓓ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "jeweling"),
    SCRIBING(Text.literal("Ⓔ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "scribing"),
    TAILORING(Text.literal("Ⓕ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "tailoring"),
    WEAPONSMITHING(Text.literal("Ⓖ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "weaponsmithing"),
    ARMOURING(Text.literal("Ⓗ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "armouring"),
    WOODWORKING(Text.literal("Ⓘ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "woodworking"),
    FARMING(Text.literal("Ⓙ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "farming", MutableText.of(new TranslatableTextContent("wytr.profession.farming.tool", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    FISHING(Text.literal("Ⓚ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "fishing", MutableText.of(new TranslatableTextContent("wytr.profession.fishing.tool", null, TranslatableTextContent.EMPTY_ARGUMENTS))),
    ALCHEMISM(Text.literal("Ⓛ ").setStyle(Style.EMPTY.withColor(Formatting.WHITE)), "alchemism");

    private final MutableText text;
    private final String key;
    private final MutableText tool;

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

    public static Profession getProfession(char icon) {
        int index = icon - 'Ⓐ';
        return values()[index];
    }

    public MutableText getText() {
        return this.text.append(MutableText.of(
                        new TranslatableTextContent("wytr.profession." + this.key, null, TranslatableTextContent.EMPTY_ARGUMENTS)));
    }

    public MutableText getTool(char tier) {
        if (this.tool != null) {
            return this.tool.append(" T" + tier);
        }
        return Text.empty();
    }
}
