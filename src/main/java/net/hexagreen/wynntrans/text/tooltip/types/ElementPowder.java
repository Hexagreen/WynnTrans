package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.Elements;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class ElementPowder extends WynnTooltipText implements ITradableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("([\uE000-\uE004] )?(Earth|Thunder|Water|Fire|Air|Blank) Powder [IV]+À*");
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString();
        if(!_iName.matches("([\uE000-\uE004] )?(Earth|Thunder|Water|Fire|Air|Blank) Powder [IV]+À*")) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        ElementPowder powder = new ElementPowder(textBowl);
        powder.translateNameSection(powder.inputText.getSiblings());
        return powder.resultText.getSiblings().getFirst().copy();
    }

    public ElementPowder(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        insertPriceSection(segments, resultText::append, (seg, i, lastIndex) -> {
            String firstLine = seg.getFirst().getString();
            if(i == 0 || firstLine.matches("^Tier \\d .+") || firstLine.matches("^Use this item at the$")) {
                translateNameSection(seg);
            }
            else if(firstLine.matches("^Effect on .+:")) {
                translateEffectSection(seg);
            }
            else if(firstLine.equals("Add this powder to your items")) {
                wrapLine(Text.translatable("wytr.item.misc.elementPowder.uses")
                        .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)), 150)
                        .forEach(resultText::append);
            }
            else if(firstLine.equals("Adding 2 powders of tier 4-6")) {
                wrapLine(Text.translatable("wytr.item.misc.elementPowder.special")
                        .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)), 150)
                        .forEach(resultText::append);
            }
            else if(firstLine.equals("Can be transformed into any")) {
                translateBlankPowderDesc(seg);
            }
            else if(firstLine.equals("Mechanically drained of its magic,")) {
                wrapLine(Text.translatable("wytr.item.misc.blankPowder.lore")
                        .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)), 150)
                        .forEach(resultText::append);
            }
            else if(firstLine.contains("Click to ")) {
                List<Text> siblings = seg.getFirst().getSiblings();
                Text icon = siblings.getFirst();
                Text instruction = switch(siblings.getLast().getString()) {
                    case " Click to Withdraw" -> Text.empty().append(" ")
                            .append(Text.translatable("wytr.func.clickToWithdraw").formatted(Formatting.GREEN));
                    case " Click to Remove" -> Text.empty().append(" ")
                            .append(Text.translatable("wytr.func.clickToRemove").formatted(Formatting.RED));
                    default -> siblings.getLast();
                };
                resultText.append(Text.empty().append(icon).append(instruction));
            }
            else {
                seg.forEach(resultText::append);
            }
            if(i != lastIndex) resultText.append(SPLITTER);
        });
    }

    private void translateNameSection(List<Text> texts) {
        texts.forEach(t -> {
            String _string = t.getString();
            if(_string.contains("Tier ")) {
                Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
                String[] split = siblings.getFirst().getString().split(" ");
                Text tier = Text.translatable("wytr.unit.tier", split[1]);
                siblings.addFirst(Text.literal(split[2]).setStyle(siblings.removeFirst().getStyle()));
                MutableText line = Text.empty().setStyle(GRAY).append(tier).append(" ");
                siblings.forEach(line::append);
                resultText.append(line);
            }
            else if(_string.contains("Blank Powder")) {
                String tier = _string.replaceFirst("Blank Powder ", "");
                resultText.append(Text.translatable("wytr.item.misc.blankPowder", tier)
                        .setStyle(t.getSiblings().getFirst().getStyle()));
            }
            else if(_string.matches("^. (Earth|Thunder|Water|Fire|Air) Powder [IV]+À*")) {
                Text elemIcon = t.getSiblings().getFirst();
                String[] split = t.getSiblings().getLast().getString().split(" ");
                Text _element = Elements.findElement(split[1]).getElement();
                Text element = Text.empty().append(elemIcon).append(" ").append(_element);
                String tier = split[3];
                resultText.append(Text.translatable("wytr.item.misc.elementPowder", element, tier)
                        .setStyle(_element.getStyle()));
            }
            else {
                resultText.append(t);
            }
        });
    }

    private void translateEffectSection(List<Text> texts) {
        texts.forEach(t -> {
            String _string = t.getString();
            Style style = t.getSiblings().getFirst().getStyle();
            if(_string.matches("^Effect on .+:")) {
                String target = _string.replaceFirst("^Effect on (.+):", "$1");
                resultText.append(Text.translatable("wytr.item.misc.elementPowder.effectOn" + target)
                        .setStyle(style));
            }
            else if(_string.matches("^- .+")) {
                Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
                String[] split = siblings.getFirst().getString().split(" ", 2);
                Text bar = Text.literal(split[0] + " ").setStyle(siblings.getFirst().getStyle());
                if(!split[1].isEmpty())
                    siblings.addFirst(Text.literal(split[1]).setStyle(siblings.removeFirst().getStyle()));
                else siblings.removeFirst();
                Text value = Text.literal(siblings.removeFirst().getString().replaceAll(" ", ""));
                Text elemIcon = siblings.removeFirst();
                if(elemIcon.getString().isBlank()) elemIcon = siblings.removeFirst();
                Text _element = siblings.removeFirst();
                Text element = Text.empty().setStyle(_element.getStyle())
                        .append(elemIcon).append(" ")
                        .append(Elements.findElement(_element.getString().replaceAll("\\W", "")).getElement());
                String attribute = siblings.removeFirst().getString().replaceAll(" ", "");
                switch(attribute) {
                    case "Damage" -> resultText.append(Text.empty().setStyle(GRAY)
                            .append(bar).append(value).append(" ")
                            .append(Text.translatable("wytr.tooltip.baseStat.damage", element)));
                    case "Defence" -> resultText.append(Text.empty().setStyle(GRAY)
                            .append(bar).append(value).append(" ")
                            .append(Text.translatable("wytr.tooltip.baseStat.defence", element)));
                    case "to" -> {
                        Text elemIcon2 = siblings.removeFirst();
                        Text _element2 = siblings.removeFirst();
                        Text element2 = Text.empty().setStyle(_element2.getStyle())
                                .append(elemIcon2).append(" ")
                                .append(Elements.findElement(_element2.getString().replaceAll("\\W", "")).getElement());
                        value = Text.literal(value.getString().replace("+", "")).setStyle(value.getStyle());
                        wrapLine(Text.empty().setStyle(GRAY).append(bar)
                                        .append(Text.translatable("wytr.item.misc.elementPowder.convert", value, element, element2))
                                , 150).forEach(resultText::append);
                    }
                }
            }
            else if(_string.contains("Ingredient Effectiveness")) {
                Text _value = t.getSiblings().getLast();
                Text value = Text.literal(_value.getString().replaceFirst(".+(\\d+%)", "$1"))
                        .setStyle(_value.getStyle());
                resultText.append(Text.translatable("wytr.item.misc.elementPowder.ingredientEffectiveness", value)
                        .setStyle(style));
            }
            else resultText.append(t);
        });
    }

    private void translateBlankPowderDesc(List<Text> texts) {
        String numericTier = "§f" + texts.get(1).getSiblings().getFirst().getString().replaceFirst("Tier ([IV]+) Powder ", "$1");
        wrapLine(Text.translatable("wytr.item.misc.blankPowder.desc", numericTier).setStyle(GRAY), 150)
                .forEach(resultText::append);
    }
}
