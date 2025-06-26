package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.tooltip.AbstractWynnItem;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class MasteryTome extends AbstractWynnItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        String tomeName = normalizeStringForKey(texts.getFirst().getString()).replaceFirst("I+$", "");
        return WTS.checkTranslationDoNotRegister("wytr.item.tome." + tomeName);
    }

    public MasteryTome(List<Text> texts) {
        super(texts);
    }

    @Override
    protected void buildStep(List<Text> seg, int step, int lastIndex) {
        String firstSibling = seg.getFirst().getSiblings().getFirst().getString();
        if(step == 0) {
            translateNameSection(seg);
        }
        else if(firstSibling.matches("[✔✖] ?")) {
            translateReqSection(seg);
        }
        else if(firstSibling.matches("[+-]\\d+(%|/[35]s| tier)?")) {
            translateIDSection(seg);
        }
        else if(firstSibling.matches(".+ Raid Reward")) {
            translateTailSection(seg);
        }
        else seg.forEach(resultText::append);
        if(step != lastIndex) resultText.append(SPLITTER);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    private void translateNameSection(List<Text> seg) {
        Text _name = seg.getFirst().getSiblings().getFirst();
        String tier = _name.getString().replaceAll("֎", "").replaceAll(".+?( I+)?$", "$1");
        Text name = Text.empty().setStyle(_name.getStyle())
                .append(Text.translatable("wytr.item.tome." + normalizeStringForKey(_name.getString()).replaceFirst("I+$", "")))
                .append(tier);
        resultText.append(name);
    }

    protected void translateTailSection(List<Text> texts) {
        for(Text t : texts) {
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
