package net.hexagreen.wynntrans;

import com.google.common.collect.Lists;
import net.minecraft.text.*;
import net.minecraft.util.Language;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class WynnTransText implements Text {
    private TextContent content;
    private List<WynnTransText> siblings;
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

    public void setContent(String key) {
        this.content = new TranslatableTextContent(key, null, TranslatableTextContent.EMPTY_ARGUMENTS);
    }

    public void setContent(String key, Object... args) {
        this.content = new TranslatableTextContent(key, null, args);
    }

    public void addSiblings(Text text) {
        this.siblings.add((WynnTransText) text);
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

    public List<WynnTransText> getWTTSiblings() {
        return this.siblings;
    }

    public WynnTransText getSiblingByIndex(int index) {
        return this.siblings.get(index);
    }
    public WynnTransText setSiblingByIndex(int index, WynnTransText text) {
        this.siblings.set(index, text);
        return this;
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
