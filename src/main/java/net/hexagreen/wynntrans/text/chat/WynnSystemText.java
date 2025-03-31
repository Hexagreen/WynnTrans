package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.types.SimpleSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class WynnSystemText extends WynnChatText {
    protected final Text originText;
    protected final MutableText header;
    protected final Text splitter;
    private final int wrappingWidth;

    protected static String removeTextBox(Text text) {
        return text.getString().replaceAll("(?<=.) ?\\n? ?\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06 ?", " ").replaceAll("\\n", "");
    }

    private static Text preprocessSystemChat(Text text, boolean isGlued) {
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

    public WynnSystemText(Text text) {
        this(text, false);
    }

    public WynnSystemText(Text text, boolean isGlued) {
        super(preprocessSystemChat(text, isGlued));
        this.originText = text;
        Style boxColor = text.getStyle().withFont(Identifier.of("minecraft:chat"));
        this.header = extractHeader(text.getSiblings().getFirst()).setStyle(boxColor);
        this.splitter = Text.literal("\n\uDAFF\uDFFC\uE001\uDB00\uDC06 ").setStyle(boxColor);
        this.wrappingWidth = setLineWrappingWidth();
    }

    @Override
    public MutableText text() {
        try {
            build();
            return attachBox(resultText);
        }
        catch(IndexOutOfBoundsException e) {
            WynnTrans.LOGGER.warn("IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(originText, "OutOfBound");
            debugClass.writeTextAsJSON(inputText, "    --    ");
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed chat message has been recorded.\n", e);
            return new SimpleSystemText(originText).text();
        }
        return originText.copy();
    }

    protected int setLineWrappingWidth() {
        return (int) ISpaceProvider.CHAT_HUD_WIDTH;
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

    private MutableText extractHeader(Text headerSibling) {
        String content = ((PlainTextContent) headerSibling.getContent()).string();
        return Text.literal(content + " ").setStyle(headerSibling.getStyle());
    }

    private MutableText attachBox(Text text) {
        MutableText result = Text.empty();
        boolean[] flag = new boolean[]{false};
        wrapLine(text, wrappingWidth).forEach(t -> {
            Text box = flag[0] ? splitter : header;
            result.append(box).append(t);
            flag[0] = true;
        });
        return result;
    }
}
