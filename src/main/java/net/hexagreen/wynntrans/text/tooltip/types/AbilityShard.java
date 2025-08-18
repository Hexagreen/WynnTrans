package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.sign.WynnSign;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class AbilityShard extends WynnTooltipText implements ITradableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("Ability ShardÀ?");
    }

    public AbilityShard(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "item.misc.AbilityShard";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        insertPriceSection(segments, resultText::append, (seg, i, last) -> {
            String _string = seg.getFirst().getString();
            if(i == 0) {
                translateNameSection(seg);
            }
            else if(_string.matches("Use this to reset your|A shard is already")) {
                translateNameSection(seg);
            }
            else if(_string.matches("Click to remove")) {
                resultText.append(Text.translatable("wytr.func.clickToRemove").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            else if(_string.contains("Quest Req:")) {
                List<Text> lastText = seg.getFirst().getSiblings();
                Text _questName = lastText.get(2);
                Text questName = Text.translatable("wytr.quest." + normalizeStringForKey(_questName.getString()))
                        .setStyle(_questName.getStyle());
                Text translated = Text.empty().append(lastText.getFirst())
                        .append(Text.translatable("wytr.requirement.quest", questName).setStyle(GRAY))
                        .append(" ")
                        .append(WynnSign.translateRecommendLevel(lastText.getLast()).setStyle(GRAY));
                resultText.append(translated);
            }
            else seg.forEach(resultText::append);
            if(i != last) resultText.append(SPLITTER);
        });
    }

    private void translateNameSection(List<Text> seg) {
        for(Text t : seg) {
            String _s = t.getString();
            if(_s.contains("Ability Shard")) {
                resultText.append(Text.translatable(translationKey).setStyle(t.getSiblings().getFirst().getStyle()));
            }
            else if(_s.equals("Use this to reset your")) {
                wrapLine(Text.translatable(translationKey + ".desc").setStyle(GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(_s.equals("A shard is already")) {
                wrapLine(Text.translatable(translationKey + ".desc.using").setStyle(GRAY), 200)
                        .forEach(resultText::append);
            }
            else if(!(_s.contains("Ability Tree") || _s.contains("present on"))) {
                resultText.append(t);
            }
        }
    }
}
