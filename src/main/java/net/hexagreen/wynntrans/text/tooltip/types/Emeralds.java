package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.tooltip.AbstractCurrencyItem;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class Emeralds extends AbstractCurrencyItem implements ISpaceProvider {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("Liquid Emerald|Emerald Block|Emerald");
    }

    public Emeralds(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    protected void translateNameStep(Text t) {
        String _string = t.getString();
        if(_string.matches("Liquid Emerald|Emerald Block|Emerald")) {
            resultText.append(Text.translatable("wytr.item.misc." + normalizeStringForKey(_string)).setStyle(t.getSiblings().getFirst().getStyle()));
        }
        else resultText.append(t);
    }

    @Override
    protected void additionalStep(String _string, List<Text> seg) {
        if(_string.contains("Transfer ")) {
            Text t = seg.getFirst();
            Text a = t.getSiblings().get(1);
            Text b = t.getSiblings().get(3);
            resultText.append(Text.translatable("wytr.item.misc.Emerald.transfer", a, b).setStyle(GRAY));
        }
        else if(_string.contains("Worth ")) {
            Text t = seg.getFirst();
            Text w = t.getSiblings().get(2);
            int width = getLongestWidth(resultText.getSiblings());
            resultText.append(centerAlign(Text.translatable("wytr.item.misc.Emerald.worth", w).formatted(Formatting.DARK_GRAY), width));
        }
    }
}
