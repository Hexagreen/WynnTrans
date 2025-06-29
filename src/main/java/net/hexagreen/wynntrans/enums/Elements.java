package net.hexagreen.wynntrans.enums;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;

public enum Elements {
    NEUTRAL(Text.translatable("wytr.element.neutral"), Text.literal("✣"), Style.EMPTY.withColor(Formatting.GOLD)),
    EARTH(Text.translatable("wytr.element.earth"), Text.literal("✤"), Style.EMPTY.withColor(Formatting.DARK_GREEN)),
    THUNDER(Text.translatable("wytr.element.thunder"), Text.literal("✦"), Style.EMPTY.withColor(Formatting.YELLOW)),
    WATER(Text.translatable("wytr.element.water"), Text.literal("❉"), Style.EMPTY.withColor(Formatting.AQUA)),
    FIRE(Text.translatable("wytr.element.fire"), Text.literal("✹"), Style.EMPTY.withColor(Formatting.RED)),
    AIR(Text.translatable("wytr.element.air"), Text.literal("❋"), Style.EMPTY.withColor(Formatting.WHITE));

    private final Text element;
    private final Text icon;
    private final Style color;

    public static Elements findElement(String string) {
        switch(string.toLowerCase(Locale.ENGLISH)) {
            case "earth" -> {
                return EARTH;
            }
            case "thunder" -> {
                return THUNDER;
            }
            case "water" -> {
                return WATER;
            }
            case "fire" -> {
                return FIRE;
            }
            case "air" -> {
                return AIR;
            }
            default -> {
                return NEUTRAL;
            }
        }
    }

    Elements(Text element, Text icon, Style color) {
        this.element = element;
        this.icon = icon;
        this.color = color;
    }

    public MutableText getElement() {
        return element.copy().setStyle(color);
    }

    public MutableText getIcon() {
        return icon.copy().setStyle(color);
    }
}
