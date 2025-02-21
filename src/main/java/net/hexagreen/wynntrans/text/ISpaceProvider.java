package net.hexagreen.wynntrans.text;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public interface ISpaceProvider {
    double CHAT_HUD_WIDTH = MinecraftClient.getInstance().inGameHud.getChatHud().getWidth()
            / MinecraftClient.getInstance().inGameHud.getChatHud().getChatScale();
    int CENTER = (int) CHAT_HUD_WIDTH / 2;
    int SPACE_WIDTH = MinecraftClient.getInstance().textRenderer.getWidth(" ");
    int SPLITTER_WIDTH = 7;

    default MutableText getSystemTextCenterIndent(Text alignTargetText) {
        int textWidth = MinecraftClient.getInstance().textRenderer.getWidth(alignTargetText);

        StringBuilder spaces = new StringBuilder();
        for(int pixels = CENTER - (int) (0.5 + textWidth / 2.0) - SPLITTER_WIDTH; pixels > 0; ) {
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

    default MutableText centerAlign(Text target) {
        return getCenterIndent(target, (int) CHAT_HUD_WIDTH).append(target);
    }

    default MutableText centerAlign(Text target, int criteriaWidth) {
        return getCenterIndent(target, criteriaWidth).append(target);
    }

    default MutableText twoColumnAlign(Text front, Text rear) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        MutableText result = Text.empty();

        int frontWidth = renderer.getWidth(front);

        StringBuilder frontSpace = new StringBuilder();
        for(int pixels = (int) (CHAT_HUD_WIDTH / 4 - (0.5 + frontWidth) / 2.0); pixels > 0; ) {
            if(pixels >= SPACE_WIDTH) {
                frontSpace.append(" ");
                pixels -= SPACE_WIDTH;
            }
            else {
                frontSpace.append("À");
                pixels -= 1;
            }
        }
        result.append(frontSpace.toString()).append(front);

        int tmpWidth = renderer.getWidth(result);
        int rearWidth = renderer.getWidth(rear);

        StringBuilder rearSpace = new StringBuilder();
        for(int pixels = (int) (CHAT_HUD_WIDTH * 0.75 - (0.5 + rearWidth) / 2.0) - tmpWidth; pixels > 0; ) {
            if(pixels >= SPACE_WIDTH) {
                rearSpace.append(" ");
                pixels -= SPACE_WIDTH;
            }
            else {
                rearSpace.append("À");
                pixels -= 1;
            }
        }
        result.append(rearSpace.toString()).append(rear);

        return result;
    }

    default MutableText getRearSpace(Text front, Text criteria) {
        return getRearSpace(front, MinecraftClient.getInstance().textRenderer.getWidth(criteria));
    }

    default MutableText getRearSpace(Text front, int maxFrontWidth) {
        int frontWidth = MinecraftClient.getInstance().textRenderer.getWidth(front);

        StringBuilder spaces = new StringBuilder();
        for(int pixels = maxFrontWidth - frontWidth; pixels > 0; ) {
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
