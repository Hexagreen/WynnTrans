package net.hexagreen.wynntrans.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public interface ICenterAligned {
    int CENTER = 40;
    int SPACE_WIDTH = MinecraftClient.getInstance().textRenderer.getWidth(" ");
    default String getCenterIndent(String alignTargetKey) {
        return getCenterIndent(MutableText.of(new TranslatableTextContent(alignTargetKey, null, TranslatableTextContent.EMPTY_ARGUMENTS)));
    }

    default String getCenterIndent(String alignTargetKey, Object... args) {
        return getCenterIndent(MutableText.of(new TranslatableTextContent(alignTargetKey, null, args)));
    }

    default String getCenterIndent(Text alignTargetText) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(alignTargetText);
        int newIndent = CENTER - (int)(0.5 + (textWidth / 2.0) / SPACE_WIDTH);

        return " ".repeat(Math.max(0, newIndent + 1));
    }
}
