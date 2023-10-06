package net.hexagreen.wynntrans.texts;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.WynnTranslationStorage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.*;
import org.slf4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class WynnText {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected static final String rootKey = "wytr.";
    protected static final String dirFunctional = "func.";
    protected static final WynnTranslationStorage WTS = WynnTrans.wynnTranslationStorage;
    protected MutableText inputText;
    protected Matcher matcher;
    protected String parentKey;
    protected MutableText resultText;

    WynnText(MutableText text, Pattern regex) {
        this.inputText = text;
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
        parentKey = setParentKey();
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

    protected String getLiteralContent() {
        return inputText.getContent().toString().equals("empty") ? "" : ((LiteralTextContent) inputText.getContent()).string();
    }

    protected String getLiteralContent(int siblingIndex) {
        return getLiteralContent(inputText.getSiblings().get(siblingIndex));
    }

    private String getLiteralContent(Text text) {
        return text.getContent().toString().equals("empty") ? "" : ((LiteralTextContent) text.getContent()).string();
    }

    protected MutableText getTranslate(String key) {
        return MutableText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS));
    }

    protected MutableText getTranslate(String key, Object... args) {
        return MutableText.of(new TranslatableTextContent(key, null, args));
    }

    protected Style getStyle() {
        return inputText.getStyle();
    }

    protected Style getStyle(int siblingIndex) {
        return inputText.getSiblings().get(siblingIndex).getStyle();
    }
}
