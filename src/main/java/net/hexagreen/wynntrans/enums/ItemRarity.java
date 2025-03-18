package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.Locale;

public enum ItemRarity {
    NORMAL(Text.translatable("wytr.rarity.normal").setStyle(Style.EMPTY.withColor(0xFFFFFF))),
    UNIQUE(Text.translatable("wytr.rarity.unique").setStyle(Style.EMPTY.withColor(0xFFFF55))),
    RARE(Text.translatable("wytr.rarity.rare").setStyle(Style.EMPTY.withColor(0xFF55FF))),
    LEGENDARY(Text.translatable("wytr.rarity.legendary").setStyle(Style.EMPTY.withColor(0x55FFFF))),
    FABLED(Text.translatable("wytr.rarity.fabled").setStyle(Style.EMPTY.withColor(0xFF5555))),
    MYTHIC(Text.translatable("wytr.rarity.mythic").setStyle(Style.EMPTY.withColor(0xAA00AA))),
    SET(Text.translatable("wytr.rarity.set").setStyle(Style.EMPTY.withColor(0x55FF55))),
    CRAFTED(Text.translatable("wytr.rarity.crafted").setStyle(Style.EMPTY.withColor(0x00AAAA)));

    private final MutableText rarity;

    public static MutableText getRarity(String rarity) {
        return valueOf(rarity.toUpperCase(Locale.ENGLISH)).rarity;
    }

    ItemRarity(MutableText rarity) {
        this.rarity = rarity;
    }

    public MutableText getRarity() {
        return rarity;
    }
}
