package net.hexagreen.wynntrans.chat;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.hexagreen.wynntrans.chat.types.SimpleText;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WynnChatText {
    protected static final String rootKey = "wytr.";
    protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
    protected final MutableText inputText;
    protected final String parentKey;
    protected static Text removedCustomNickname = null;
    protected Matcher matcher;
    protected MutableText resultText;

    public WynnChatText(Text text, Pattern regex) {
        this.inputText = (MutableText) text;
        this.parentKey = setParentKey();
        if(regex != null) {
            this.matcher = createMatcher(text, regex);
            boolean ignore = this.matcher.find();
        }
    }

    /**
     * Set {@code baseKey} for translation.
     * @return Key for translation
     */
    protected abstract String setParentKey();

    /**
     * Method for creating translatable text
     */
    protected abstract void build() throws IndexOutOfBoundsException, UnprocessedChatTypeException;

    @SuppressWarnings("DataFlowIssue")
    public boolean print() {
        Text printLine = text();
        if(printLine == null) return true;
        MinecraftClient.getInstance().player.sendMessage(printLine);
        return true;
    }

    public MutableText text() {
        try {
            build();
            return resultText;
        } catch(IndexOutOfBoundsException e) {
            LogUtils.getLogger().warn("[WynnTrans] IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OutOfBound");
        } catch(UnprocessedChatTypeException e) {
            LogUtils.getLogger().warn("[WynnTrans] Unprocessed chat message has been recorded.\n", e);
            return new SimpleText(inputText, null).text();
        }
        return inputText;
    }

    protected Matcher createMatcher(Text text, Pattern regex) {
        return regex.matcher(text.getString());
    }

    /**
     * Get player name from sibling.
     * <p>
     * This method detects sibling would contain Custom nickname (Champion Feature) or not.
     * @param index Index of sibling contains Custom Nickname form text
     * @return Custom nickname or Original name as {@code Text}
     */
    protected Text getPlayerNameFromSibling(int index) {
        String name = getContentString(index);
        if("".equals(name)) {
            return getSibling(index).getSiblings().getFirst();
        }
        else {
            return getSibling(index);
        }
    }

    /**
     * Get {@code String} of {@code inputText}'s content.
     * @return Returns empty {@code String} if content is empty, otherwise {@code content.getString()}
     */
    protected String getContentString() {
        return inputText.getContent().toString().equals("empty") ? "" : ((PlainTextContent) inputText.getContent()).string();
    }

    /**
     * Get {@code String} from {@code inputText}'s sibling.
     * @return Returns empty {@code String} if sibling's content is empty, otherwise some {@code String}
     * */
    protected String getContentString(int siblingIndex) {
        return getContentString(inputText.getSiblings().get(siblingIndex));
    }

    /**
     * Get {@code String} from given {@code Text}'s content.
     * @param text Target text
     * @return Returns empty {@code String} if content is empty, otherwise {@code content.getString()}
     */
    private String getContentString(Text text) {
        return text.getContent().toString().equals("empty") ? "" : ((PlainTextContent) text.getContent()).string();
    }

    /**
     * Makes {@code MutableText} contains translatable form content.
     * @param key Translation key
     * @return {@code MutableText} contains translatable form content.
     */
    protected MutableText newTranslate(String key) {
        return MutableText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS));
    }

    /**
     * Makes {@code MutableText} contains translatable form content.
     * @param key Translation key
     * @param args Translation arguments
     * @return {@code MutableText} contains translatable form content.
     */
    protected MutableText newTranslate(String key, Object... args) {
        return MutableText.of(new TranslatableTextContent(key, null, args));
    }

    /**
     * Get {@code Style} from {@code inputText}'s content.
     * @return {@code inputText.getStyle()}
     */
    protected Style getStyle() {
        return inputText.getStyle();
    }

    /**
     * Get {@code Style} from {@code inputText}'s sibling.
     * @param siblingIndex Index of target sibling
     * @return {@code inputText.getStyle()}
     */
    protected Style getStyle(int siblingIndex) {
        return inputText.getSiblings().get(siblingIndex).getStyle();
    }

    /**
     * Get {@code Text} from {@code inputText}'s sibling.
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
        for(int i = 0; i < codes.length; i++) {
            formatting[i] = Formatting.byCode(codes[i]);
        }
        return Style.EMPTY.withFormatting(formatting);
    }

    protected static Text removeCustomNicknameFromDialog(Text text) {
        MutableText newText = text.copyContentOnly().setStyle(text.getStyle());
        newText.append(text.getSiblings().get(0));
        String partial = "";
        Style style = text.getSiblings().get(1).getStyle();
        for(int index = 1; text.getSiblings().size() > index; index++) {
            Text sibling = text.getSiblings().get(index);
            if(!sibling.getContent().equals(PlainTextContent.EMPTY)) {
                if(style.equals(sibling.getStyle())) {
                    partial = partial.concat(sibling.getString());
                }
                else {
                    newText.append(Text.literal(partial).setStyle(style));
                    partial = sibling.getString();
                    style = sibling.getStyle();
                }
            }
            else {
                removedCustomNickname = sibling.copy();
                if(style.equals(sibling.getStyle())) {
                    partial = partial.concat("%1$s");
                }
                else {
                    newText.append(Text.literal(partial).setStyle(style));
                    partial = "%1$s";
                    style = sibling.getStyle();
                }
            }
        }
        newText.append(Text.literal(partial).setStyle(style));

        return newText;
    }

    protected String normalizeStringAreaName(String string) {
        return string.replace(" ", "").replace(".","")
                .replace("'", "").replace("-", "")
                .replace("À", "").replace("֎", "");
    }

    protected String normalizeStringCaveName(String string) {
        return string.replace(" ", "").replace("'", "")
                .replace("À", "").replace("֎", "");
    }

    protected String normalizeStringQuestName(String string) {
        return string.replace(" ", "").replace("'", "")
                .replace("À", "").replace("֎", "");
    }

    protected String normalizeStringNPCName(String string) {
        return string.replace(" ", "").replace(".", "")
                .replace("'", "").replace(":", "");
    }

    protected String normalizeStringWorldEventName(String string) {
        return string.replace(" ", "").replace("'", "")
                .replace("%", "").replace("-", "");
    }

    public static class UnprocessedChatTypeException extends RuntimeException {
        public UnprocessedChatTypeException(String className) {
            super(className + " has thrown Exception.");
        }
    }
}
