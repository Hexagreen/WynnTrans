package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class CorkianAugment extends WynnTooltipText implements ITradableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("Corkian (Amplifier|Insulator|Simulator)( I+)?À?");
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString();
        if(!_iName.matches("Corkian (Amplifier|Insulator|Simulator)( I+)?À?")) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        CorkianAugment corkianAugment = new CorkianAugment(textBowl);
        corkianAugment.translateName(corkianAugment.inputText.getSiblings(), new String[]{""});
        return corkianAugment.resultText.getSiblings().getFirst().copy();
    }

    public CorkianAugment(List<Text> texts) {
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
        String[] key = {""};
        insertPriceSection(segments, resultText::append, (seg, i, lastIndex) -> {
            String _string = seg.getFirst().getString();
            if(i == 0) {
                translateName(seg, key);
            }
            else if(key[0].isEmpty()) seg.forEach(resultText::append);
            else if(_string.contains("Incompatible with ")) return;
            else if(_string.matches("Blocks the roll count from|Locks an identification from")) {
                wrapLine(Text.translatable(key[0] + ".desc").formatted(Formatting.GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.matches("Increases all positive identifications")) {
                Text percent = seg.get(1).getSiblings().get(1);
                wrapLine(Text.translatable(key[0] + ".desc", percent).formatted(Formatting.GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.matches(".+ Corkian Augment")) {
                Text rarity = ItemRarity.getRarity(_string.replaceFirst(" Corkian Augment", ""));
                Text line = Text.empty().setStyle(rarity.getStyle())
                        .append(rarity).append(" ").append(Text.translatable("wytr.item.misc.corkianAugment"));
                resultText.append(line);
                wrapLine(Text.translatable("wytr.item.misc.corkianAugment.desc").formatted(Formatting.DARK_GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_string.equals("Click to Remove")) {
                resultText.append(Text.translatable("wytr.func.clickToRemove").formatted(Formatting.RED));
            }
            else seg.forEach(resultText::append);
            if(i != lastIndex) resultText.append(SPLITTER);
        });
    }

    private void translateName(List<Text> seg, String[] key) {
        String _string = seg.getFirst().getString();
        Style textStyle = seg.getFirst().getSiblings().getFirst().getStyle();
        if(_string.contains("Amplifier")) {
            String tier = _string.replaceAll("À", "").replaceFirst("Corkian Amplifier ?", "");
            key[0] = "wytr.item.misc.corkianAugment.amplifier";
            resultText.append(Text.translatable(key[0], tier).setStyle(textStyle));
        }
        else if(_string.contains("Simulator")) {
            key[0] = "wytr.item.misc.corkianAugment.simulator";
            resultText.append(Text.translatable(key[0]).setStyle(textStyle));
        }
        else if(_string.contains("Insulator")) {
            key[0] = "wytr.item.misc.corkianAugment.insulator";
            resultText.append(Text.translatable(key[0]).setStyle(textStyle));
        }
        else seg.forEach(resultText::append);
    }
}
