package net.hexagreen.wynntrans.text;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WynnTransText {

    protected static final String rootKey = "wytr.";
    protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
    private static final Pattern colorParser = Pattern.compile("(?:§.)*(?<!§)[^§]+");
    protected final String translationKey;
    protected final MutableText inputText;
    protected MutableText resultText;

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
        String styleCode = codeOrTextString.replaceFirst("(?s).*?((?:§[0123456789abcdefklnmor])+).*", "$1");
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

    public WynnTransText(Text text) {
        this.inputText = (MutableText) text;
        this.translationKey = setTranslationKey();
    }

    /**
     * Set {@code baseKey} for translation.
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

    protected String normalizeStringForKey(String string) {
        return string.replaceAll("[ .,'À֎’:&%()\"-]", "");
    }

    public static class TextTranslationFailException extends RuntimeException {

        public TextTranslationFailException(String className) {
            super(className + " has thrown Exception.");
        }
    }
}