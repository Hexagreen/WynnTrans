package net.hexagreen.wynntrans.text.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public interface ICenterAligned {
    int CENTER = 160;
    int SPACE_WIDTH = MinecraftClient.getInstance().textRenderer.getWidth(" ");
    default MutableText getCenterIndent(String alignTargetKey) {
        return getCenterIndent(MutableText.of(new TranslatableTextContent(alignTargetKey, null, TranslatableTextContent.EMPTY_ARGUMENTS)));
    }

    default MutableText getCenterIndent(String alignTargetKey, Object... args) {
        return getCenterIndent(MutableText.of(new TranslatableTextContent(alignTargetKey, null, args)));
    }

    default MutableText getCenterIndent(Text alignTargetText) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(alignTargetText);

        StringBuilder spaces = new StringBuilder();
        for(int pixels = CENTER - (int)(0.5 + textWidth / 2.0); pixels > 0;) {
            if(pixels >= SPACE_WIDTH) {
                spaces.append(" ");
                pixels -= SPACE_WIDTH;
            }
            else {
                spaces.append("À");
                pixels -= 1;
            }
        }

        return Text.literal(spaces.toString());
    }
}
