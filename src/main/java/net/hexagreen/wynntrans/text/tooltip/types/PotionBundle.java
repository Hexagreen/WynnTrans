package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.EquipmentAndConsumables;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;

public class PotionBundle extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("§c\\[\\+\\d+ ❤] §dPotions of Healing §4\\[\\d+/\\d+]");
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString();
        if(!Pattern.compile("Potions of Healing").matcher(_iName).find()) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        PotionBundle bundle = new PotionBundle(textBowl);
        bundle.translateNameSection(bundle.inputText.getSiblings());
        return bundle.resultText.getSiblings().getFirst().copy();
    }

    public PotionBundle(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        for(int i = 0, size = segments.size(); i < size; i++) {
            List<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.contains("Potions of Healing")) {
                translateNameSection(seg);
            }
            else if(_string.contains("Tier ")) {
                translatePotionsSection(seg);
            }
            else seg.forEach(resultText::append);
            if(i != size - 1) resultText.append(SPLITTER);
        }
    }

    private void translateNameSection(List<Text> segment) {
        for(int i = 0, size = segment.size(); i < size; i++) {
            switch(i) {
                case 0 -> {
                    Deque<Text> siblings = new ArrayDeque<>(segment.get(i).getSiblings());
                    Text line = Text.empty().append(siblings.removeFirst())
                            .append(Text.translatable("wytr.tooltip.potionBundle").setStyle(siblings.removeFirst().getStyle()))
                            .append(" ")
                            .append(siblings.removeFirst());
                    resultText.append(line);
                }
                case 1 -> resultText.append(Text.translatable("wytr.tooltip.potionBundle.drink"));
                case 2 -> resultText.append(Text.translatable("wytr.tooltip.potionBundle.open"));
            }
        }
    }

    private void translatePotionsSection(List<Text> segment) {
        segment.forEach(t -> {
            Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
            Text _tier = siblings.removeLast();
            Text tier;
            if(_tier.getString().contains("Crafted ")) {
                Text rarity = ItemRarity.CRAFTED.getRarity().copyContentOnly();
                String crafted = _tier.getString().replaceAll(" \\(Crafted |\\)", "");
                tier = Text.empty().append(rarity).append(" ").append(EquipmentAndConsumables.getText(crafted));
            }
            else {
                String tierNumber = _tier.getString().replaceAll(" \\(Tier |\\)", "");
                tier = Text.translatable("wytr.unit.tier", tierNumber);
            }
            siblings.addLast(Text.empty().setStyle(GRAY).append(" (").append(tier).append(")"));
            MutableText line = Text.empty();
            siblings.forEach(line::append);
            resultText.append(line);
        });
    }
}
