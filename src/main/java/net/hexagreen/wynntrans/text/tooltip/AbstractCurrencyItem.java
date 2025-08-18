package net.hexagreen.wynntrans.text.tooltip;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractCurrencyItem extends WynnTooltipText implements ITradableItem {
    protected Consumer<Text> textAppender;
    protected Consumer<Text> optionalTextAppender;

    public AbstractCurrencyItem(List<Text> texts) {
        super(texts);
        this.textAppender = resultText::append;
        this.optionalTextAppender = resultText::append;
    }

    @Override
    protected final void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        insertPriceSection(segments, textAppender, this::buildStep);
    }

    protected final void buildStep(List<Text> seg, int step, final int lastIndex) {
        String _string = seg.getFirst().getString();
        if(step == 0) translateNameSection(seg);
        else additionalStep(_string, seg);
        if(step != lastIndex) textAppender.accept(SPLITTER);
    }

    protected final void translateNameSection(List<Text> texts) {
        for(Text text : texts) {
            if(text.getString().contains("\uE002\uE014\uE011\uE011\uE004\uE00D\uE002\uE018\uDB00\uDC02"))
                textAppender.accept(text);
            else translateNameStep(text);
        }
    }

    protected abstract void translateNameStep(Text text);

    protected abstract void additionalStep(String _string, List<Text> seg);
}
