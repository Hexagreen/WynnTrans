package net.hexagreen.wynntrans.text.chat;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.chat.types.SimpleSystemText;
import net.minecraft.text.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WynnSystemText extends WynnChatText {
    protected final Text originText;
    protected final MutableText header;
    protected final Text splitter;

    protected static Text preprocessSystemChat(Text text, boolean isGlued) {
        if(isGlued) {
            MutableText glued = Text.empty();
            for(Text line : text.getSiblings()) {
                Text phase1 = removeHeaderSplitter(line);
                glued.append(mergeSiblings(extractSiblings(Objects.requireNonNull(phase1))));
            }
            return glued;
        }
        else {
            Text phase1 = removeHeaderSplitter(text);
            return mergeSiblings(extractSiblings(Objects.requireNonNull(phase1)));
        }
    }

    private static Text mergeSiblings(List<Text> texts) {
        StringBuilder stringBuilder = new StringBuilder();
        Style style = texts.getFirst().getStyle();
        MutableText result = Text.empty().setStyle(style);
        for(Text element : texts) {
            if(element.getString().isEmpty()) continue;
            if(element.getStyle().equals(Style.EMPTY) || element.getStyle().equals(style)) {
                stringBuilder.append(element.copyContentOnly().getLiteralString());
            }
            else {
                if(!stringBuilder.isEmpty()) {
                    result.append(Text.literal(stringBuilder.toString()).setStyle(style));
                }
                stringBuilder = new StringBuilder(Objects.requireNonNull(element.copyContentOnly().getLiteralString()));
                style = element.getStyle();
            }
        }
        return result.append(Text.literal(stringBuilder.toString()).setStyle(style));
    }

    private static List<Text> extractSiblings(Text text) {
        List<Text> textList = new ArrayList<>();
        textList.add(text.copyContentOnly().setStyle(text.getStyle()));
        if(!text.getSiblings().isEmpty()) {
            for(Text sibling : text.getSiblings()) {
                textList.addAll(extractSiblings(sibling.copy().setStyle(sibling.getStyle().withParent(text.getStyle()))));
            }
        }
        return textList;
    }

    @Nullable
    private static Text removeHeaderSplitter(Text text) {
        MutableText result = reformTextContent(text);
        if(result != null) {
            for(Text sibling : text.getSiblings()) {
                Text child = removeHeaderSplitter(sibling);
                if(child != null) result.append(child);
            }
            if(result.getString().isEmpty() && result.getStyle().equals(Style.EMPTY)) return null;
        }
        return result;
    }

    @Nullable
    private static MutableText reformTextContent(Text text) {
        if(text.getContent().equals(PlainTextContent.EMPTY)) return Text.empty().setStyle(text.getStyle());
        String content = text.copyContentOnly().getString();
        Style style = text.getStyle();

        if(style.getFont().equals(Identifier.of("minecraft:space"))) return null;
        if(content.matches("\\uDAFF\\uDFFC.\\uDAFF\\uDFFF\\uE002\\uDAFF\\uDFFE|\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06")) {
            return null;
        }
        content = content.replaceFirst("^ ", "").replaceFirst(" ?\\n$", "\n ");

        return Text.literal(content).setStyle(style);
    }

    public WynnSystemText(Text text, Pattern regex) {
        super(preprocessSystemChat(text, false), regex);
        this.originText = text;
        this.header = extractHeader(text.getSiblings().getFirst());
        this.splitter = Text.literal("\n\uDAFF\uDFFC\uE001\uDB00\uDC06").setStyle(text.getStyle().withFont(Identifier.of("minecraft:chat")));
    }

    public WynnSystemText(Text text, Pattern regex, boolean isGlued) {
        super(preprocessSystemChat(text, isGlued), regex);
        this.originText = text;
        this.header = extractHeader(text.getSiblings().getFirst().getSiblings().getFirst());
        this.splitter = Text.literal("\n\uDAFF\uDFFC\uE001\uDB00\uDC06").setStyle(text.getStyle().withFont(Identifier.of("minecraft:chat")));
    }

    @Override
    public MutableText text() {
        try {
            build();
            return resultText;
        }
        catch(IndexOutOfBoundsException e) {
            LogUtils.getLogger().warn("[WynnTrans] IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OutOfBound");
        }
        catch(TextTranslationFailException e) {
            LogUtils.getLogger().warn("[WynnTrans] Unprocessed chat message has been recorded.\n", e);
            return new SimpleSystemText(originText, null).text();
        }
        return originText.copy();
    }

    @Override
    protected Matcher createMatcher(Text text, Pattern regex) {
        return regex.matcher(text.getString().replaceAll("\\n", ""));
    }

    /**
     * Makes {@code MutableText} contains translatable form content. <p>
     * First translation argument is {@code splitter}
     *
     * @param key Translation key
     * @return {@code MutableText} contains translatable form content.
     */
    protected MutableText newTranslateWithSplit(String key) {
        return newTranslate(key, splitter);
    }

    /**
     * Makes {@code MutableText} contains translatable form content. <p>
     * First translation argument is {@code splitter}
     *
     * @param key  Translation key
     * @param args Translation arguments
     * @return {@code MutableText} contains translatable form content.
     */
    protected MutableText newTranslateWithSplit(String key, Object... args) {
        List<Object> arguments = new ArrayList<>();
        arguments.addFirst(splitter);
        arguments.addAll(Arrays.asList(args));
        return MutableText.of(new TranslatableTextContent(key, null, arguments.toArray()));
    }

    /**
     * Replace linefeed character to placeholder for {@code splitter} <p>
     * Use for create translation registration value string
     *
     * @param string Target for replace linefeed to placeholder
     * @return String with all linefeed replaced
     */
    protected String lineFeedReplacer(String string) {
        return string.replaceAll("\\n", "%1\\$s");
    }

    /**
     * Removes linefeed character from parameter string <p>
     * Use for create translation key
     *
     * @param string Target for remove linefeed
     * @return String with all linefeed removed
     */
    protected String lineFeedRemover(String string) {
        return string.replaceAll("\\n", "");
    }

    protected String replacerRemover(String string) {
        return string.replaceAll("%1\\$s", "");
    }

    private MutableText extractHeader(Text headerSibling) {
        String content = ((PlainTextContent) headerSibling.getContent()).string();
        return Text.literal(content + " ").setStyle(headerSibling.getStyle());
    }
}
