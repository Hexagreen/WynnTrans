package net.hexagreen.wynntrans.text.tooltip;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public interface IPricedItem {

    Style GOLD = Style.EMPTY.withColor(Formatting.GOLD);
    MutableText PRICE_TEXT = Text.translatable("wytr.func.price").setStyle(GOLD);

    default ItemTooltipPriceSection detachPriceSection(List<List<Text>> segments) {
        int priceSectionIndex = -1;
        List<Text> priceSection = new ArrayList<>();
        for(int i = 1, size = segments.size(); i < size; i++) {
            List<Text> seg = segments.get(i);
            if(seg.getFirst().getString().contains("Price:")) {
                priceSectionIndex = i;
                segments.remove(i).forEach(t -> {
                    if(t.getString().equals("Price:")) {
                        priceSection.add(PRICE_TEXT);
                    }
                    else {
                        priceSection.add(t);
                    }
                });
                break;
            }
        }

        return new ItemTooltipPriceSection(priceSection, priceSectionIndex);
    }

    record ItemTooltipPriceSection(List<Text> priceTexts, int position) {
    }
}
