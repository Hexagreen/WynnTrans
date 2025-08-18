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

import java.util.Optional;

public abstract class WynnSystemText extends WynnChatText {
    protected final Text originText;
    protected final MutableText header;
    protected final Text splitter;
    private final int wrappingWidth;

    protected static String removeTextBox(Text text) {
        return text.getString()
                .replaceAll("(?<=.) ?\\n? ?\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06 ?", " ")
                .replaceAll("\\n", "");
    }

    private static Text preprocessSystemChat(Text text, boolean isGlued) {
        if(isGlued) {
            MutableText glued = Text.empty();
            for(Text line : text.getSiblings()) {
                glued.append(mergeSiblingsWithVisitor(line));
            }
            return glued;
        }
        else {
            return mergeSiblingsWithVisitor(text);
        }
    }

    private static Text mergeSiblingsWithVisitor(Text root) {
        StringBuilder sb = new StringBuilder();
        MutableText result = Text.empty().setStyle(root.getStyle());
        final Style[] currentStyle = {root.getStyle()};

        root.visit((style, str) -> {
            if(root.getContent().equals(PlainTextContent.EMPTY) && str.isEmpty()) {
                return Optional.empty();
            }

            if(str.matches("\\uDAFF\\uDFFC.\\uDAFF\\uDFFF\\uE002\\uDAFF\\uDFFE|\\uDAFF\\uDFFC\\uE001\\uDB00\\uDC06")) {
                return Optional.empty();
            }

            str = str.replaceFirst(" ?\\n$", "\n");

            if(result.getString().isEmpty() && sb.isEmpty()) str = str.replaceFirst("^ ", "");
            if(str.isEmpty()) return Optional.empty();

            if(str.matches("§.")) {
                return Optional.empty();
            }

            if(currentStyle[0].equals(Style.EMPTY)) {
                currentStyle[0] = style;
            }

            if(style.equals(Style.EMPTY) || style.equals(currentStyle[0]) || str.isBlank()) {
                sb.append(str);
            }
            else {
                if(!sb.isEmpty()) {
                    result.append(Text.literal(sb.toString()).setStyle(currentStyle[0]));
                }
                sb.setLength(0);
                sb.append(str);
                currentStyle[0] = style;
            }

            return Optional.empty();
        }, Style.EMPTY);

        if(!sb.isEmpty()) {
            result.append(Text.literal(sb.toString()).setStyle(currentStyle[0]));
        }

        return result;
    }

    public WynnSystemText(Text text) {
        this(text, false);
    }

    public WynnSystemText(Text text, boolean isGlued) {
        super(preprocessSystemChat(text, isGlued));
        this.originText = text;
        Style boxColor = isGlued ? text.getSiblings().getFirst().getStyle() : text.getStyle();
        boxColor = boxColor.withFont(Identifier.of("minecraft:chat/prefix"));
        this.header = extractHeader(text.getSiblings().getFirst(), isGlued).setStyle(boxColor).append(" ");
        this.splitter = Text.literal("\n\uDAFF\uDFFC\uE001\uDB00\uDC06  ").setStyle(boxColor);
        this.wrappingWidth = setLineWrappingWidth();
    }

    @Override
    public MutableText text() {
        try {
            build();
            return attachBox(resultText);
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed chat message has been recorded.\n", e);
            return new SimpleSystemText(originText).text();
        }
        catch(Exception e) {
            WynnTrans.LOGGER.warn("Exception occurred.\n", e);
            debugClass.writeTextAsJSON(originText, "Exception");
            debugClass.writeTextAsJSON(inputText, "    --    ");
        }
        return originText.copy();
    }

    @Override
    public boolean print() {
        Text printLine = text();
        if(printLine == null) return true;
        transportMessage(originText, printLine);
        return true;
    }

    protected int setLineWrappingWidth() {
        return (int) ISpaceProvider.CHAT_HUD_WIDTH - 15;
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

    private MutableText extractHeader(Text headerSibling, boolean isGlued) {
        String content = isGlued ? headerSibling.getSiblings().getFirst().getString() : ((PlainTextContent) headerSibling.getContent()).string();
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
