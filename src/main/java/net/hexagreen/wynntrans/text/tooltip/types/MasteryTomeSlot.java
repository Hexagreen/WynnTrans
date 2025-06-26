package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class MasteryTomeSlot extends WynnTooltipText {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.contains("\uE005")) return false;
        return texts.getFirst().getString().matches("§5.+ Mastery Tome Slot");
    }

    public MasteryTomeSlot(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text _name = getSibling(0).getSiblings().getFirst();
        String type = _name.getString().replaceFirst(" Mastery Tome Slot", "");
        resultText.append(Text.translatable("wytr.tooltip.masteryTomeSlot." + type).setStyle(_name.getStyle()));
        resultText.append(" ");
        resultText.append(Text.translatable("wytr.tooltip.masteryTomeSlot.req").formatted(Formatting.GOLD));
        Deque<Text> reqSiblings = new ArrayDeque<>(getSibling(-1).getSiblings());
        MutableText line = Text.empty();
        line.append(reqSiblings.removeFirst()).append(reqSiblings.removeFirst());
        String level = reqSiblings.removeFirst().getString().replaceAll("\\D", "");
        line.append(Text.translatable("wytr.requirement.combat", level).setStyle(GRAY));
        resultText.append(line);
    }
}
