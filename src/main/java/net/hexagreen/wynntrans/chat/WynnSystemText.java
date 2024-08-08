package net.hexagreen.wynntrans.chat;

import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class WynnSystemText extends WynnChatText {
    protected final Text originText;
    protected final MutableText header;
    protected final Text splitter;
    protected final String valHint;

    public WynnSystemText(Text text, Pattern regex) {
        super(preprocessSystemChat(text), regex);
        this.originText = text;
        this.header = extractHeader(text.getSiblings().getFirst());
        this.splitter = Text.literal("\n\uDAFF\uDFFC\uE001\uDB00\uDC06").setStyle(text.getStyle().withFont(Identifier.of("minecraft:chat")));
        this.valHint = text.getString()
                .replaceFirst("^\\uDAFF\\uDFFC.\\uDAFF\\uDFFF\\uE002\\uDAFF\\uDFFE |^\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06 ", "")
                .replaceAll("\\n\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06", "%1\\$s");
    }

    /**
     * Replace linefeed character to placeholder for {@code splitter} <p>
     * Use for create translation registration value string
     * @param string Target for replace linefeed to placeholder
     * @return String with all linefeed replaced
     */
    protected String lineFeedReplacer(String string) {
        return string.replaceAll("\\n", "%1\\$s");
    }

    /**
     * Removes linefeed character from parameter string <p>
     * Use for create translation key
     * @param string Target for remove linefeed
     * @return String with all linefeed removed
     */
    protected String lineFeedRemover(String string) {
        return string.replaceAll("\\n", "");
    }

    private MutableText extractHeader(Text headerSibling) {
        String content = ((PlainTextContent) headerSibling.getContent()).string();
        return Text.literal(content + " ").setStyle(headerSibling.getStyle());
    }

    private static Text preprocessSystemChat(Text text) {
        Text phase1 = removeHeaderSplitter(text);
        return mergeSiblings(extractSiblings(Objects.requireNonNull(phase1)));
    }

    private static Text mergeSiblings(List<Text> texts) {
        StringBuilder stringBuilder = new StringBuilder();
        Style style = texts.getFirst().getStyle();
        MutableText result = Text.empty().setStyle(style);
        for(Text sibling : texts) {
            if(sibling.getString().isEmpty()) continue;
            if(sibling.getStyle().equals(Style.EMPTY) || sibling.getStyle().equals(style)) {
                stringBuilder.append(sibling.copyContentOnly().getLiteralString());
            }
            else {
                result.append(Text.literal(stringBuilder.toString()).setStyle(style));
                stringBuilder = new StringBuilder("" + sibling.copyContentOnly().getLiteralString());
                style = sibling.getStyle();
            }
        }
        return result.append(Text.literal(stringBuilder.toString()).setStyle(style));
    }

    private static List<Text> extractSiblings(Text text) {
        List<Text> textList = new ArrayList<>();
        textList.add(text.copyContentOnly().setStyle(text.getStyle()));
        if(text.getSiblings().isEmpty()) return textList;
        else {
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
        String content = text.getContent() instanceof PlainTextContent ? ((PlainTextContent) text.getContent()).string() : "";
        Style style = text.getStyle();

        if(content.matches("\\uDAFF\\uDFFC.\\uDAFF\\uDFFF\\uE002\\uDAFF\\uDFFE|\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06")) {
            return null;
        }
        content = content.replaceFirst("^ ", "").replaceFirst(" ?\\n$", "\n ");

        return Text.literal(content).setStyle(style);
    }
}
