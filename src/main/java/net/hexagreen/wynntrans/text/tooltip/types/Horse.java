package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class Horse extends WynnTooltipText implements ITradableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("§f(Brown|Black|Chestnut|White) HorseÀ*");
    }

    public Horse(List<Text> texts) {
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
        insertPriceSection(segments, resultText::append, (seg, i, lastIndex) -> {
            for(Text text : seg) {
                Style color = text.getSiblings().getFirst().getStyle();
                String _string = text.getString();
                if(_string.contains("Horse")) {
                    resultText.append(Text.translatable("wytr.item.misc." + normalizeStringForKey(_string)).setStyle(color));
                }
                else if(_string.contains("Tier")) {
                    String num = _string.replaceAll("\\D", "");
                    resultText.append(Text.translatable("wytr.unit.tier", num).setStyle(color));
                }
                else if(_string.contains("Speed")) {
                    String num = _string.split(": ")[1];
                    resultText.append(Text.translatable("wytr.tooltip.horse.speed", num).setStyle(color));
                }
                else if(_string.contains("Jump")) {
                    String num = _string.split(": ")[1];
                    resultText.append(Text.translatable("wytr.tooltip.horse.jump", num).setStyle(color));
                }
                else if(_string.contains("Armour")) {
                    String num = _string.split(": ")[1];
                    resultText.append(Text.translatable("wytr.tooltip.horse.armour", num).setStyle(color));
                }
                else if(_string.contains("Xp")) {
                    String num = _string.split(": ")[1];
                    resultText.append(Text.translatable("wytr.tooltip.horse.xp", num).setStyle(color));
                }
                else if(_string.contains("Name")) {
                    String name = _string.split(": ")[1];
                    resultText.append(Text.translatable("wytr.tooltip.horse.name", name).setStyle(color));
                }
                else resultText.append(text);
            }
        });
    }
}
