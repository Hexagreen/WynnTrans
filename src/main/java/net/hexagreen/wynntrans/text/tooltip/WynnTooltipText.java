package net.hexagreen.wynntrans.text.tooltip;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.tooltip.types.SimpleTooltip;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public abstract class WynnTooltipText extends WynnTransText {
    private static boolean lever = false;
    private static boolean registered = true;

    protected static List<Text> colorCodedToStyledBatch(List<Text> textList) {
        return textList.parallelStream().map(WynnTooltipText::colorCodedToStyled).toList();
    }

    public WynnTooltipText(List<Text> text) {
        super(siblingsToText(text));
        resultText = Text.empty();
    }

    public List<Text> text() {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_SUBTRACT) == 1) {
            textRecorder(getSiblings());
            return getSiblings();
        }
        try {
            build();
            textRecorder(resultText.getSiblings());
            return resultText.getSiblings();
        }
        catch(IndexOutOfBoundsException e) {
            LogUtils.getLogger().warn("[WynnTrans] IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OOB - Tooltip");
        }
        catch(TextTranslationFailException e) {
            LogUtils.getLogger().warn("[WynnTrans] Unprocessed chat message has been recorded.\n", e);
            return new SimpleTooltip(getSiblings()).text();
        }
        textRecorder(getSiblings());
        return getSiblings();
    }

    public MutableText textAsMutable() {
        try {
            build();
            return resultText;
        }
        catch(IndexOutOfBoundsException e) {
            LogUtils.getLogger().warn("[WynnTrans] IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OOB - Tooltip");
        }
        catch(TextTranslationFailException e) {
            LogUtils.getLogger().warn("[WynnTrans] Unprocessed chat message has been recorded.\n", e);
        }
        return inputText;
    }

    protected void textRecorder(List<Text> texts) {
        boolean rawMode = false;
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_ADD) == 0) lever = false;
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_ADD) == 1 && !lever) registered = false;
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_ALT) == 1) rawMode = true;
        List<Text> target = texts.isEmpty() ? getSiblings() : texts;
        if(!rawMode) target = colorCodedToStyledBatch(target);

        if(!registered) {
            WynnTrans.wynnTranslationStorage.recordUnregisteredTooltip(target, "Tooltip");
            registered = true;
            lever = true;
        }
    }

    protected Text mergeTextStyleSide(Text text) {
        return mergeTextStyleSide(new Text[]{text});
    }

    protected Text mergeTextStyleSide(Text[] texts) {
        MutableText result = Text.empty();
        AtomicReference<StringBuilder> newBody = new AtomicReference<>(new StringBuilder());
        AtomicReference<Style> newStyle = new AtomicReference<>(texts[0].getSiblings().getFirst().getStyle());
        for(Text text : texts) {
            text.visit((style, string) -> {
                if(string.isEmpty()) return Optional.empty();
                if(!style.equals(newStyle.get())) {
                    result.append(Text.literal(newBody.get().toString()).setStyle(newStyle.get()));
                    newBody.set(new StringBuilder());
                    newStyle.set(style);
                }
                newBody.get().append(string);
                return Optional.empty();
            }, Style.EMPTY);
            newBody.get().append(" ");
        }
        if(!newBody.get().isEmpty()) {
            String done = newBody.get().toString().replaceFirst(" +$", "");
            result.append(Text.literal(done).setStyle(newStyle.get()));
        }
        return result;
    }

    protected int getLongestWidth(List<Text> lines) {
        return getLongestWidth(lines, Text.literal(" "));
    }

    protected int getLongestWidth(List<Text> lines, Text criteria) {
        return getLongestWidth(lines, MinecraftClient.getInstance().textRenderer.getWidth(criteria));
    }

    protected int getLongestWidth(List<Text> lines, int criteria) {
        int max = criteria;
        for(Text line : lines) {
            max = Math.max(max, MinecraftClient.getInstance().textRenderer.getWidth(line));
        }
        return max;
    }
}
