package net.hexagreen.wynntrans.text;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public interface ISpaceProvider {
    TextRenderer TEXT_RENDERER = MinecraftClient.getInstance().textRenderer;
    double CHAT_HUD_WIDTH = MinecraftClient.getInstance().inGameHud.getChatHud().getWidth()
            / MinecraftClient.getInstance().inGameHud.getChatHud().getChatScale();
    int CENTER = (int) CHAT_HUD_WIDTH / 2;
    int SPACE_WIDTH = TEXT_RENDERER.getWidth(" ");
    int SPLITTER_WIDTH = 7;

    default MutableText getSystemTextCenterIndent(Text alignTargetText) {
        int textWidth = TEXT_RENDERER.getWidth(alignTargetText);

        StringBuilder spaces = getBlanks(CENTER - (int) (0.5 + textWidth / 2.0) - SPLITTER_WIDTH);

        return Text.literal(spaces.toString());
    }

    default MutableText getCenterIndent(Text target, int criteriaWidth) {
        int textWidth = TEXT_RENDERER.getWidth(target);
        if(textWidth >= criteriaWidth) return Text.empty();

        StringBuilder spaces = getBlanks((criteriaWidth / 2) - (int) (0.5 + textWidth / 2.0));

        return Text.literal(spaces.toString());
    }

    default MutableText centerAlign(Text target) {
        return getCenterIndent(target, (int) CHAT_HUD_WIDTH).append(target);
    }

    default MutableText centerAlign(Text target, int criteriaWidth) {
        return getCenterIndent(target, criteriaWidth).append(target);
    }

    default MutableText twoColumnAlign(Text front, Text rear) {
        TextRenderer renderer = TEXT_RENDERER;
        MutableText result = Text.empty();

        int frontWidth = renderer.getWidth(front);

        StringBuilder frontSpace = getBlanks((int) (CHAT_HUD_WIDTH / 4 - (0.5 + frontWidth) / 2.0));
        result.append(frontSpace.toString()).append(front);

        int tmpWidth = renderer.getWidth(result);
        int rearWidth = renderer.getWidth(rear);

        StringBuilder rearSpace = getBlanks((int) (CHAT_HUD_WIDTH * 0.75 - (0.5 + rearWidth) / 2.0) - tmpWidth);
        result.append(rearSpace.toString()).append(rear);

        return result;
    }

    default MutableText getRearSpace(Text front, int maxFrontWidth) {
        int frontWidth = TEXT_RENDERER.getWidth(front);

        StringBuilder spaces = getBlanks(maxFrontWidth - frontWidth);
        spaces.append(" ");

        return Text.literal(spaces.toString());
    }

    default StringBuilder getBlanks(int spaceWidth) {
        StringBuilder spaces = new StringBuilder();
        for(int pixels = spaceWidth; pixels > 0; ) {
            if(pixels >= SPACE_WIDTH) {
                spaces.append(" ");
                pixels -= SPACE_WIDTH;
            }
            else {
                spaces.append("À");
                pixels -= 1;
            }
        }
        return spaces;
    }
}
