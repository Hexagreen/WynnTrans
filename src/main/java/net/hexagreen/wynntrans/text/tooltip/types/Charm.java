package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.tooltip.AbstractIdentifiableItem;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class Charm extends AbstractIdentifiableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 2) return false;
        String itemName = texts.getFirst().getString().replaceAll("^Unidentified |^Perfect |^Defective |\\[.+%]$", "");
        String normalized = normalizeStringForKey(itemName);
        return WTS.checkTranslationDoNotRegister("wytr.item.charm." + normalized);
    }

    public Charm(List<Text> texts) {
        super(texts);
    }

    @Override
    protected void buildStep(List<Text> seg, int step, int lastIndex) {
        String firstLine = seg.getFirst().getString();
        String firstSibling = seg.getFirst().getSiblings().getFirst().getString();
        if(step == 0) {
            translateNameSection(seg);
        }
        else if(firstLine.contains("Active while on inventory")) {
            translateNameSection(seg);
        }
        else if(firstSibling.matches("[✔✖] ?")) {
            translateReqSection(seg);
        }
        else if(firstSibling.matches("[+-]\\d+(%|/[35]s| tier)?")) {
            translateIDSection(seg);
        }
        else if(firstLine.matches(".+ Raid Reward")) {
            translateTailSection(seg);
        }
        else {
            seg.forEach(resultText::append);
        }
        if(step != lastIndex) resultText.append(SPLITTER);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    private void translateNameSection(List<Text> segment) {
        for(Text t : segment) {
            String _s = t.getString();
            String normalized = normalizeStringForKey(_s.replaceFirst(" \\[.+%]", ""));
            if(WTS.checkTranslationDoNotRegister("wytr.item.charm." + normalized)) {
                Style nameStyle = t.getSiblings().getFirst().getStyle();
                Text percent = _s.matches(".+ \\[.+%]") ? t.getSiblings().getLast() : Text.empty();
                resultText.append(Text.translatable("wytr.item.charm." + normalized).setStyle(nameStyle).append(percent));
            }
            else if(_s.equals("Active while on inventory")) {
                resultText.append(Text.translatable("wytr.tooltip.charm.desc").formatted(Formatting.DARK_GRAY));
            }
            else resultText.append(t);
        }
    }

    private void translateTailSection(List<Text> segment) {
        for(Text t : segment) {
            String string = t.getString();
            if(string.matches("Untradable Item")) {
                resultText.append(Text.translatable("wytr.tooltip.item.untradable").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            else if(string.matches(".+ Raid Reward$")) {
                String[] splits = string.split(" ", 2);
                Text rarity = ItemRarity.getRarity(splits[0]);
                resultText.append(Text.translatable("wytr.tooltip.item.raidReward", rarity).setStyle(rarity.getStyle()));
            }
            else resultText.append(t);
        }
    }
}
