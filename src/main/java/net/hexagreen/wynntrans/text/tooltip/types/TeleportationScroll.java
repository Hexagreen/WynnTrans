package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class TeleportationScroll extends WynnTooltipText implements ITradableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches(".+ Teleportation ScrollÀ*( \\[\\d/\\d])?");
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString();
        if(!_iName.matches(".+ Teleportation Scroll( \\[\\d/\\d])?")) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        TeleportationScroll teleScroll = new TeleportationScroll(textBowl);
        teleScroll.translateName(teleScroll.inputText.getSiblings());
        return teleScroll.resultText.getSiblings().getFirst().copy();
    }

    public TeleportationScroll(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> siblings = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), siblings);
        insertPriceSection(siblings, resultText::append, (seg, i, lastIndex) -> {
            String _string = seg.getFirst().getString();
            if(i == 0 || _string.contains("Warps to ")) translateName(seg);
            else if(_string.contains(" Right-Click to use")) translateKeybind(seg);
            else if(_string.equals("Charges are restored every 10m")) {
                resultText.append(Text.translatable("wytr.item.misc.teleportationScroll.desc").formatted(Formatting.DARK_GRAY));
            }
            else if(_string.equals("Teleportation scrolls can be used")) {
                wrapLine(Text.translatable("wytr.item.misc.teleportationScroll.desc2").formatted(Formatting.GRAY), 200)
                        .forEach(resultText::append);
            }
            else {
                seg.forEach(resultText::append);
            }
            if(i != lastIndex) resultText.append(SPLITTER);
        });
    }

    private void translateName(List<Text> segment) {
        segment.forEach(t -> {
            Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
            if(siblings.isEmpty()) return;
            Text _name = siblings.removeFirst();

            if(_name.getString().contains("Teleportation Scroll")) {
                String _location = _name.getString().replaceFirst(" Teleportation Scroll ?", "");
                Text location = Text.translatable("wytr.area." + normalizeStringForKey(_location));
                MutableText line = Text.translatable("wytr.item.misc.teleportationScroll", location).setStyle(_name.getStyle());
                if(!siblings.isEmpty()) line.append(" ").append(siblings.removeFirst());
                resultText.append(line);
            }
            else if(_name.getString().contains("Warps to")) {
                MutableText destination = Text.empty();
                siblings.forEach(destination::append);
                resultText.append(Text.translatable("wytr.item.misc.teleportationScroll.dest", destination).setStyle(_name.getStyle()));
            }
        });
    }

    private void translateKeybind(List<Text> segment) {
        Deque<Text> siblings = new ArrayDeque<>(segment.getFirst().getSiblings());
        Text _keybind = siblings.removeLast();
        MutableText line = Text.empty();
        siblings.forEach(line::append);
        line.append(" ");
        resultText.append(line.append(Text.translatable("wytr.func.rClickToUse").setStyle(_keybind.getStyle())));
    }
}
