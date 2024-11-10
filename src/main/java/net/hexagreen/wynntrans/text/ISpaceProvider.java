package net.hexagreen.wynntrans.text;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

public interface ISpaceProvider {
    int CENTER = (int) ((double) MinecraftClient.getInstance().inGameHud.getChatHud().getWidth() / 2
            / MinecraftClient.getInstance().inGameHud.getChatHud().getChatScale());
    int SPACE_WIDTH = MinecraftClient.getInstance().textRenderer.getWidth(" ");

    default MutableText getCenterIndent(String alignTargetKey) {
        return getCenterIndent(MutableText.of(new TranslatableTextContent(alignTargetKey, null, TranslatableTextContent.EMPTY_ARGUMENTS)));
    }

    default MutableText getCenterIndent(String alignTargetKey, Object... args) {
        return getCenterIndent(MutableText.of(new TranslatableTextContent(alignTargetKey, null, args)));
    }

//    default MutableText getSystemTextCenterIndent(Text alignTargetText) {
//
//    }

    default MutableText getCenterIndent(Text alignTargetText) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(alignTargetText);

        StringBuilder spaces = new StringBuilder();
        for(int pixels = CENTER - (int) (0.5 + textWidth / 2.0); pixels > 0; ) {
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

    default MutableText getCenterIndent(Text target, Text criteria) {
        int criteriaWidth = MinecraftClient.getInstance().textRenderer.getWidth(criteria);
        return getCenterIndent(target, criteriaWidth);
    }

    default MutableText getCenterIndent(Text target, int criteriaWidth) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(target);
        if(textWidth >= criteriaWidth) return Text.empty();

        StringBuilder spaces = new StringBuilder();
        for(int pixels = (criteriaWidth / 2) - (int) (0.5 + textWidth / 2.0); pixels > 0; ) {
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

    default MutableText getRearSpace(Text front, Text criteria) {
        return getRearSpace(front, MinecraftClient.getInstance().textRenderer.getWidth(criteria));
    }

    default MutableText getRearSpace(Text front, int rearWidth) {
        int frontWidth = MinecraftClient.getInstance().textRenderer.getWidth(front);

        StringBuilder spaces = new StringBuilder();
        for(int pixels = rearWidth - frontWidth; pixels > 0; ) {
            if(pixels >= SPACE_WIDTH) {
                spaces.append(" ");
                pixels -= SPACE_WIDTH;
            }
            else {
                spaces.append("À");
                pixels -= 1;
            }
        }
        spaces.append(" ");

        return Text.literal(spaces.toString());
    }
}
