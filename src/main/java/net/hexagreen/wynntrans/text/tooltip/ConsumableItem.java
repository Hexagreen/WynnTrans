package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.text.ITime;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public abstract class ConsumableItem extends AbstractWynnItem {

    public ConsumableItem(List<Text> texts) {
        super(texts);
    }

    @Override
    protected void buildStep(List<Text> seg, int step, int lastIndex) {
        String _string = seg.getFirst().getString();
        if(step == 0) translateNameSection(seg);
        else if(_string.equals("Effect:")) translateEffectSection(seg);
        else if(_string.matches("^[✔✖] .+")) translateReqSection(seg);
        else additionalStep(_string, seg);
        if(step != lastIndex) textAppender.accept(SPLITTER);
        else postStep();
    }

    protected abstract void additionalStep(String _string, List<Text> seg);

    protected abstract void postStep();

    protected abstract void translateNameSection(List<Text> texts);

    protected void translateEffectSection(List<Text> texts) {
        List<Text> effects = texts.subList(1, texts.size());
        textAppender.accept(Text.translatable("wytr.tooltip.consumable.effect")
                .setStyle(texts.getFirst().getSiblings().getFirst().getStyle()));
        effects.forEach(t -> {
            Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
            Text bar = siblings.removeFirst();
            Text info = siblings.removeFirst();
            MutableText body = Text.empty().append(bar);
            if(info.getString().equals("Duration: ")) {
                body.append(Text.translatable("wytr.tooltip.consumable.duration"));
                body.append(ITime.translateTime(siblings.removeFirst()));
            }
            else buildEffectBody(body, info, siblings);
            textAppender.accept(body);
        });
    }

    protected abstract void buildEffectBody(MutableText body, Text info, Deque<Text> siblings);
}
