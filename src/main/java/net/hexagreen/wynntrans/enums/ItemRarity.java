package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Formatting;

public enum ItemRarity {
    NORMAL(MutableText.of(new TranslatableTextContent("wytr.rarity.normal", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.WHITE))),
    UNIQUE(MutableText.of(new TranslatableTextContent("wytr.rarity.unique", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.YELLOW))),
    RARE(MutableText.of(new TranslatableTextContent("wytr.rarity.rare", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE))),
    LEGENDARY(MutableText.of(new TranslatableTextContent("wytr.rarity.legendary", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
    FABLED(MutableText.of(new TranslatableTextContent("wytr.rarity.fabled", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.RED))),
    MYTHIC(MutableText.of(new TranslatableTextContent("wytr.rarity.mythic", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE))),
    SET(MutableText.of(new TranslatableTextContent("wytr.rarity.set", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.GREEN))),
    CRAFTED(MutableText.of(new TranslatableTextContent("wytr.rarity.crafted", null, TranslatableTextContent.EMPTY_ARGUMENTS)).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA))),
    ;

    private final Text rarity;

    ItemRarity(Text rarity) {
        this.rarity = rarity;
    }

    public Text getRarity() {
        return rarity;
    }
}
