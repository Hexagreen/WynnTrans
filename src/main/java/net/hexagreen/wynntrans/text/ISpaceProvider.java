package net.hexagreen.wynntrans.text;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public interface ISpaceProvider {
    TextRenderer TEXT_RENDERER = MinecraftClient.getInstance().textRenderer;
    double CHAT_HUD_WIDTH = MinecraftClient.getInstance().inGameHud.getChatHud().getWidth()
            / MinecraftClient.getInstance().inGameHud.getChatHud().getChatScale();
    int CENTER = (int) CHAT_HUD_WIDTH / 2;
    int SYSTEM_TEXT_BOX_WIDTH = 7;
    Text SYSTEM_TEXT_ADJUSTMENT = Text.literal("\uDAFF\uDFF9")
            .setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:space")));

    default MutableText getSystemTextCenterIndent(Text alignTargetText) {
        int textWidth = TEXT_RENDERER.getWidth(alignTargetText);

        return getBlanks(CENTER - (int) (0.5 + textWidth / 2.0) - SYSTEM_TEXT_BOX_WIDTH);
    }

    default MutableText getCenterIndent(Text target, int criteriaWidth) {
        int textWidth = TEXT_RENDERER.getWidth(target);
        if(textWidth >= criteriaWidth) return Text.empty();

        return getBlanks((criteriaWidth / 2) - (int) (0.5 + textWidth / 2.0));
    }

    default MutableText centerAlign(Text target) {
        return getCenterIndent(target, (int) CHAT_HUD_WIDTH).append(target);
    }

    default MutableText centerAlign(Text target, int criteriaWidth) {
        return getCenterIndent(target, criteriaWidth).append(target);
    }

    default MutableText twoColumnAlign(Text front, Text rear) {
        return twoColumnAlignWithSplitter(front, rear, Text.empty());
    }

    default MutableText twoColumnAlign(Text front, Text rear, int criteriaWidth) {
        return twoColumnAlignWithSplitter(front, rear, Text.empty(), criteriaWidth);
    }

    default MutableText twoColumnAlignWithSplitter(Text front, Text rear, Text splitter) {
        return twoColumnAlignWithSplitter(front, rear, splitter, (int) CHAT_HUD_WIDTH);
    }

    default MutableText twoColumnAlignWithSplitter(Text front, Text rear, Text splitter, int criteriaWidth) {
        TextRenderer renderer = TEXT_RENDERER;
        MutableText result = Text.empty();

        int frontWidth = renderer.getWidth(front);

        MutableText frontSpace = getBlanks((int) (criteriaWidth * 0.25 - (0.5 + frontWidth) / 2.0));
        result.append(frontSpace).append(front);

        int splitterWidth = renderer.getWidth(splitter);
        int tmpWidth = renderer.getWidth(result);

        if(splitterWidth > 0) {
            MutableText splitterSpace = getBlanks((int) (criteriaWidth * 0.5 - (0.5 + splitterWidth) / 2.0) - tmpWidth);
            result.append(splitterSpace).append(splitter);
            tmpWidth = renderer.getWidth(result);
        }

        int rearWidth = renderer.getWidth(rear);

        MutableText rearSpace = getBlanks((int) (criteriaWidth * 0.75 - (0.5 + rearWidth) / 2.0) - tmpWidth);
        result.append(rearSpace).append(rear);

        return result;
    }

    default MutableText getRearSpace(Text front, int maxFrontWidth) {
        int frontWidth = TEXT_RENDERER.getWidth(front);

        return getBlanks(maxFrontWidth - frontWidth).append(" ");
    }

    default MutableText getBlanks(int spaceWidth) {
        if(spaceWidth < 0 || spaceWidth >= 1024) {
            return Text.empty();
        }
        return Text.empty().append(Text.literal(String.valueOf((char) 0xDB00) + (char) (0xDC00 + spaceWidth))
                .setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:space"))));
    }
}