package net.hexagreen.wynntrans.text;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

public abstract class WynnTransText {
    protected static final String rootKey = "wytr.";
    protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
    protected final String parentKey;
    protected final MutableText inputText;
    protected MutableText resultText;

    public WynnTransText(Text text) {
        this.inputText = (MutableText) text;
        this.parentKey = setParentKey();
    }

    /**
     * Set {@code baseKey} for translation.
     * @return Key for translation
     */
    protected abstract String setParentKey();

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
        return getContentString(inputText.getSiblings().get(siblingIndex));
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
     * Makes {@code MutableText} contains translatable form content.
     *
     * @param key Translation key
     * @return {@code MutableText} contains translatable form content.
     */
    protected MutableText newTranslate(String key) {
        return MutableText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS));
    }

    /**
     * Makes {@code MutableText} contains translatable form content.
     *
     * @param key  Translation key
     * @param args Translation arguments
     * @return {@code MutableText} contains translatable form content.
     */
    protected MutableText newTranslate(String key, Object... args) {
        return MutableText.of(new TranslatableTextContent(key, null, args));
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
        return inputText.getSiblings().get(siblingIndex).getStyle();
    }

    /**
     * Get {@code Text} from {@code inputText}'s sibling.
     *
     * @param siblingIndex Index of target, it allows negative value (ex. -1 will return last one)
     * @return Some sibling that index refers.
     */
    protected Text getSibling(int siblingIndex) {
        siblingIndex = siblingIndex < 0 ? inputText.getSiblings().size() + siblingIndex : siblingIndex;
        return inputText.getSiblings().get(siblingIndex);
    }

    protected Style parseStyleCode(String styleCode) {
        char[] codes = styleCode.replace("§", "").toCharArray();
        Formatting[] formatting = new Formatting[codes.length];
        for (int i = 0; i < codes.length; i++) {
            formatting[i] = Formatting.byCode(codes[i]);
        }
        return Style.EMPTY.withFormatting(formatting);
    }

    protected String normalizeStringAreaName(String string) {
        return string.replace(" ", "").replace(".","")
                .replace("'", "").replace("-", "")
                .replace("À", "").replace("֎", "")
                .replace("’", "");
    }

    protected String normalizeStringCaveName(String string) {
        return string.replace(" ", "").replace("'", "")
                .replace("À", "").replace("֎", "")
                .replace("’", "");
    }

    protected String normalizeStringQuestName(String string) {
        return string.replace(" ", "").replace("'", "")
                .replace("À", "").replace("֎", "")
                .replace("’", "");
    }

    protected String normalizeStringNPCName(String string) {
        return string.replace(" ", "").replace(".", "")
                .replace("'", "").replace(":", "")
                .replace("’", "").replace("\"", "");
    }

    protected String normalizeStringWorldEventName(String string) {
        return string.replace(" ", "").replace("'", "")
                .replace("%", "").replace("-", "")
                .replace("’", "");
    }

    public static class TextTranslationFailException extends RuntimeException {
        public TextTranslationFailException(String className) {
            super(className + " has thrown Exception.");
        }
    }
}