package net.hexagreen.wynntrans.chat;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import org.slf4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WynnChatText {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static final String rootKey = "wytr.";
    protected static final String dirFunctional = "func.";
    protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
    protected final MutableText inputText;
    protected Matcher matcher;
    protected String parentKey;
    protected MutableText resultText;

    WynnChatText(Text text, Pattern regex) {
        this.inputText = (MutableText) text;
        this.parentKey = setParentKey();
        if(regex != null) {
            this.matcher = regex.matcher(text.getString());
            this.matcher.find();
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
    protected abstract void build();

    @SuppressWarnings("DataFlowIssue")
    public void print() {
        build();
        MinecraftClient.getInstance().player.sendMessage(resultText);
    }

//    protected WynnTransText getPlayerName(Text text, int index) {
//        String name = getLiteralContent(text);
//        if("".equals(name)) {
//            return WynnTransText.of(text.getSiblings().get(index).getSiblings().get(0));
//        }
//        else {
//            return WynnTransText.of(text.getSiblings().get(index));
//        }
//    }

    protected String getContentLiteral() {
        return inputText.getContent().toString().equals("empty") ? "" : ((LiteralTextContent) inputText.getContent()).string();
    }

    protected String getContentLiteral(int siblingIndex) {
        return getContentLiteral(inputText.getSiblings().get(siblingIndex));
    }

    private String getContentLiteral(Text text) {
        return text.getContent().toString().equals("empty") ? "" : ((LiteralTextContent) text.getContent()).string();
    }

    protected MutableText newTranslate(String key) {
        return MutableText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS));
    }

    protected MutableText newTranslate(String key, Object... args) {
        return MutableText.of(new TranslatableTextContent(key, null, args));
    }

    protected Style getStyle() {
        return inputText.getStyle();
    }

    protected Style getStyle(int siblingIndex) {
        return inputText.getSiblings().get(siblingIndex).getStyle();
    }
}
