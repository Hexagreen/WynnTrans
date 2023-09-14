package net.hexagreen.wynntrans;

import com.google.common.collect.Lists;
import net.minecraft.text.*;
import net.minecraft.util.Language;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class WynnTransText implements Text {
    private TextContent content;
    private final List<WynnTransText> siblings;
    private Style style;
    private OrderedText ordered;
    @Nullable
    private Language language;

    WynnTransText(TextContent content, List<Text> siblings, Style style) {
        this.content = content;
        this.siblings = Lists.newArrayList();
        this.style = style;
        this.ordered = OrderedText.EMPTY;
        for(Text sibling : siblings) {
            this.siblings.add(WynnTransText.of(sibling));
        }
    }

    public static WynnTransText of(Text text) {
        return new WynnTransText(text.getContent(), text.getSiblings(), text.getStyle());
    }

    public static WynnTransText of(TextContent content) {
        return new WynnTransText(content, Lists.newArrayList(), Style.EMPTY);
    }

    public void setTranslateContent(String key) {
        this.content = new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS);
    }

    public void setTranslateContent(String key, Object... args) {
        this.content = new TranslatableTextContent(key, null, args);
    }

    public void addTranslateSibling(String key) {
        this.addSibling(WynnTransText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS)));
    }

    public void addTranslateSibling(int index, String key) {
        this.addSibling(index, WynnTransText.of(new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS)));
    }

    public void addTranslateSibling(int index, String key, Object... args) {
        this.addSibling(index, WynnTransText.of(new TranslatableTextContent(key, null, args)));
    }

    public void addSibling(Text text) {
        this.siblings.add((WynnTransText) text);
    }

    public void addSibling(WynnTransText text) {
        this.siblings.add(text);
    }

    public void addSibling(int index, WynnTransText text) {
        this.siblings.add(index, text);
    }

    public void removeSibling(int from, int to) {
        int count = to - from + 1;
        while (count > 0 && from < this.siblings.size()) {
            this.siblings.remove(from);
        }

    }

    public WynnTransText getSiblingByIndex(int index) {
        return this.siblings.get(index);
    }
    public void setSiblingByIndex(WynnTransText text, int index) {
        this.siblings.set(index, text);
    }

    public void addStyle(Style style) {
        this.style = style;
    }

    public void removeStyle() {
        this.style = Style.EMPTY;
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    @Override
    public TextContent getContent() {
        return this.content;
    }

    @Override
    public List<Text> getSiblings() {
        return this.siblings.stream().map(Text.class::cast).collect(Collectors.toList());
    }

    @Override
    public OrderedText asOrderedText() {
        Language language = Language.getInstance();
        if (this.language != language) {
            this.ordered = language.reorder(this);
            this.language = language;
        }

        return this.ordered;
    }
}
