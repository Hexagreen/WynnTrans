package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.sign.WynnSign;
import net.hexagreen.wynntrans.text.tooltip.IPricedItem;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class AbilityShard extends WynnTooltipText implements ITooltipSplitter, IPricedItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("Ability ShardÀ?");
    }

    public AbilityShard(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "item.misc.AbilityShard";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> dump = new ArrayList<>();
        dump.add(Text.translatable(translationKey).setStyle(getSibling(0).getSiblings().getFirst().getStyle()));

        boolean selling = getSibling(0).getString().contains("À");
        if(selling) {
            List<List<Text>> segments = new ArrayList<>();
            splitTooltipToSegments(getSiblings(), segments);
            ItemTooltipPriceSection ps = detachPriceSection(segments);
            dump.add(SPLITTER);
            dump.addAll(ps.priceTexts());
            dump.add(SPLITTER);
        }

        Text lastText = getSibling(-1);
        boolean clickToRemove = lastText.getString().equals("Click to remove");
        String k = clickToRemove ? translationKey + ".desc.using" : translationKey + ".desc";
        dump.addAll(wrapLine(Text.translatable(k).setStyle(GRAY), 200));
        if(selling) {
            dump.forEach(resultText::append);
            return;
        }
        dump.add(SPLITTER);

        if(clickToRemove) {
            dump.add(Text.translatable("wytr.func.clickToRemove").setStyle(Style.EMPTY.withColor(Formatting.RED)));
        }
        else {
            Text _questName = lastText.getSiblings().get(2);
            Text questName = Text.translatable("wytr.quest." + normalizeStringForKey(_questName.getString()))
                    .setStyle(_questName.getStyle());
            Text translated = Text.empty().append(lastText.getSiblings().getFirst())
                    .append(Text.translatable("wytr.requirement.quest", questName).setStyle(GRAY))
                    .append(" ")
                    .append(WynnSign.translateRecommendLevel(lastText.getSiblings().getLast()).setStyle(GRAY));
            dump.add(translated);
        }

        dump.forEach(resultText::append);
    }
}
