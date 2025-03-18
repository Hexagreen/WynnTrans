package net.hexagreen.wynntrans.text;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WynnTransText {

    protected static final String rootKey = "wytr.";
    protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
    protected static final TextHandler handler = MinecraftClient.getInstance().textRenderer.getTextHandler();
    private static final Pattern colorParser = Pattern.compile("(?:§.)*(?<!§)[^§]+");
    protected final String translationKey;
    protected final MutableText inputText;
    protected MutableText resultText;

    protected static Text flatText(Text text) {
        MutableText linear = text.copyContentOnly().setStyle(text.getStyle());
        for(Text sibling : text.getSiblings()) {
            sibling.visit((s, t) -> {
                linear.append(Text.literal(t).setStyle(s));
                return Optional.empty();
            }, text.getStyle());
        }
        return linear;
    }

    protected static Text colorCodedToStyled(Text text) {
        List<Text> list = new ArrayList<>();
        text.visit((style, asString) -> {
            if(asString.isEmpty()) return Optional.empty();
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

    protected static Text siblingsToText(List<Text> texts) {
        MutableText result = Text.empty();
        for(Text text : texts) {
            result.append(text);
        }
        return result;
    }

    protected static Style parseStyleCode(String codeOrTextString) {
        Matcher matcher = Pattern.compile("^((?:§[0123456789abcdefklnmor])+)").matcher(codeOrTextString.replaceFirst("^\\s+", ""));
        if(!matcher.find()) return Style.EMPTY;
        String styleCode = matcher.group(1);
        if(styleCode.isBlank()) return Style.EMPTY;
        char[] codes = styleCode.replace("§", "")
                .replaceAll("[^0123456789abcdefklmnor]", "")
                .replaceAll(".*r", "")
                .toCharArray();
        Formatting[] formatting = new Formatting[codes.length];
        for(int i = 0; i < codes.length; i++) {
            formatting[i] = Formatting.byCode(codes[i]);
        }
        return Style.EMPTY.withFormatting(formatting);
    }

    protected static String normalizeStringForKey(String string) {
        return string.replaceAll("[ .,'À֎’:&%()\"-]", "");
    }

    public WynnTransText(Text text) {
        this.inputText = (MutableText) text;
        this.translationKey = setTranslationKey();
    }

    /**
     * Set {@code translationKey} for translation.
     *
     * @return Key for translation
     */
    protected abstract String setTranslationKey();

    /**
     * Method for creating translatable text
     */
    protected abstract void build() throws IndexOutOfBoundsException, TextTranslationFailException;

    /**
     * Get {@code String} of {@code inputText}'s content.
     *
     * @return Returns empty {@code String} if content is empty, otherwise {@code content.getString()}
     */
    protected String getContentString() {
        return inputText.getContent().toString().equals("empty") ? "" : ((PlainTextContent) inputText.getContent()).string();
    }

    /**
     * Get {@code String} from {@code inputText}'s sibling.
     *
     * @return Returns empty {@code String} if sibling's content is empty, otherwise some {@code String}
     */
    protected String getContentString(int siblingIndex) {
        return getContentString(getSiblings().get(siblingIndex));
    }

    /**
     * Get {@code String} from given {@code Text}'s content.
     *
     * @param text Target text
     * @return Returns empty {@code String} if content is empty, otherwise {@code content.getString()}
     */
    private String getContentString(Text text) {
        return text.getContent().toString().equals("empty") ? "" : ((PlainTextContent) text.getContent()).string();
    }

    /**
     * Get {@code Style} from {@code inputText}'s content.
     *
     * @return {@code inputText.getStyle()}
     */
    protected Style getStyle() {
        return inputText.getStyle();
    }

    /**
     * Get {@code Style} from {@code inputText}'s sibling.
     *
     * @param siblingIndex Index of target sibling
     * @return {@code inputText.getStyle()}
     */
    protected Style getStyle(int siblingIndex) {
        return getSiblings().get(siblingIndex).getStyle();
    }

    /**
     * Get {@code Text} from {@code inputText}'s sibling.
     *
     * @param siblingIndex Index of target, it allows negative value (ex. -1 will return last one)
     * @return Some sibling that index refers.
     */
    protected Text getSibling(int siblingIndex) {
        siblingIndex = siblingIndex < 0 ? getSiblings().size() + siblingIndex : siblingIndex;
        return getSiblings().get(siblingIndex);
    }

    protected List<Text> getSiblings() {
        return inputText.getSiblings();
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

    protected Text styleToColorCode(List<Text> siblings, Style overrideStyle) {
        StringBuilder stringBuilder = new StringBuilder();
        TextColor oColor = overrideStyle.getColor();
        boolean oBold = overrideStyle.isBold();
        boolean oItalic = overrideStyle.isItalic();
        boolean oUnder = overrideStyle.isUnderlined();
        boolean oStrike = overrideStyle.isStrikethrough();
        boolean oObfuscated = overrideStyle.isObfuscated();
        boolean codeAppended = false;

        for(Text text : siblings) {
            Style style = text.getStyle();
            TextColor color = style.getColor();

            if(codeAppended) {
                stringBuilder.append(Formatting.RESET);
                codeAppended = false;
            }
            if(color != null && !color.equals(oColor)) {
                Formatting formatting = Formatting.byName(color.getName());
                if(formatting != null) stringBuilder.append(formatting);
                codeAppended = true;
            }
            if(style.isBold() && !oBold) {
                stringBuilder.append(Formatting.BOLD);
                codeAppended = true;
            }
            if(style.isItalic() && !oItalic) {
                stringBuilder.append(Formatting.ITALIC);
                codeAppended = true;
            }
            if(style.isUnderlined() && !oUnder) {
                stringBuilder.append(Formatting.UNDERLINE);
                codeAppended = true;
            }
            if(style.isStrikethrough() && !oStrike) {
                stringBuilder.append(Formatting.STRIKETHROUGH);
                codeAppended = true;
            }
            if(style.isObfuscated() && !oObfuscated) {
                stringBuilder.append(Formatting.OBFUSCATED);
                codeAppended = true;
            }
            stringBuilder.append(text.getString());
        }
        return Text.literal(stringBuilder.toString()).setStyle(overrideStyle);
    }

    protected Text rainbowDecoration(Text text) {
        String[] rainbows = {"§4", "§c", "§6", "§e", "§a", "§2", "§b", "§9", "§5", "§d"};
        StringBuilder sb = new StringBuilder();
        int ignore = text.getString().codePoints().reduce(0, (index, cp) -> {
            if(cp == " ".codePointAt(0)) {
                sb.append(rainbows[index]).append(Character.toChars(cp));
                return index + 1;
            }
            else {
                sb.append(Character.toChars(cp));
                return index;
            }
        });

        return Text.literal(sb.toString()).setStyle(text.getStyle());
    }

    public static class TextTranslationFailException extends RuntimeException {

        public TextTranslationFailException(String className) {
            super(className + " has thrown Exception.");
        }
    }
}