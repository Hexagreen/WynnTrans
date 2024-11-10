package net.hexagreen.wynntrans.text.tooltip;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.tooltip.types.SimpleTooltip;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.StringVisitable;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WynnTooltipText extends WynnTransText {

    private static final Pattern colorParser = Pattern.compile("(?:§.)*(?<!§)[^§]+");
    private static final TextHandler handler = MinecraftClient.getInstance().textRenderer.getTextHandler();
    private static boolean lever = false;
    private static boolean registered = true;

    protected static Text colorCodedToStyled(Text text) {
        List<Text> list = new ArrayList<>();
        text.visit((style, asString) -> {
            if(!asString.contains("§")) {
                list.add(Text.literal(asString).setStyle(style));
                return Optional.empty();
            }
            list.addAll(colorCodedToStyled(asString, style));
            return Optional.empty();
        }, Style.EMPTY);

        return siblingsToText(list);
    }

    private static List<Text> colorCodedToStyled(String textAsString, Style parentStyle) {
        Matcher matcher = colorParser.matcher(textAsString);
        List<String> segments = new ArrayList<>();
        while(matcher.find()) {
            segments.add(matcher.group());
        }
        List<Text> result = new ArrayList<>();
        for(String segment : segments) {
            String content = segment.replaceFirst("(?:§.)+", "");
            Style style = parseStyleCode(segment).withParent(parentStyle);
            result.add(Text.literal(content).setStyle(style));
        }
        return result;
    }

    protected static List<Text> colorCodedToStyledBatch(List<Text> textList) {
        return textList.parallelStream().map(WynnTooltipText::colorCodedToStyled).toList();
    }

    private static Text siblingsToText(List<Text> texts) {
        MutableText result = Text.empty();
        for(Text text : texts) {
            result.append(text);
        }
        return result;
    }

    public WynnTooltipText(List<Text> text) {
        super(siblingsToText(text));
        resultText = Text.empty();
    }

    public List<Text> text() {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_SUBTRACT) == 1) return getSiblings();
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
        textRecorder(inputText.getSiblings());
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

    protected List<Text> wrapLine(Text text, int length) {
        List<StringVisitable> svs = handler.wrapLines(text, length, Style.EMPTY);
        List<Text> wrapped = new ArrayList<>();
        for(StringVisitable sv : svs) {
            MutableText tmp = Text.empty();
            sv.visit((style, string) -> {
                tmp.append(Text.literal(string).setStyle(style));
                return Optional.empty();
            }, Style.EMPTY);
            wrapped.add(tmp);
        }
        return wrapped;
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
                if(string.isBlank()) return Optional.empty();
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
