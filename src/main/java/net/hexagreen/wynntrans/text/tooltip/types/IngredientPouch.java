package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class IngredientPouch extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().equals("§6Ingredient Pouch")
                || texts.getFirst().getString().equals("§cClick to confirm");
    }

    public IngredientPouch(List<Text> texts) {
        super(texts, true);
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
            if(i == 0) {
                translateNameSection(segments.getFirst());
                resultText.append(SPLITTER);
                continue;
            }

            List<Text> seg = segments.get(i);
            String firstLineString = seg.getFirst().getString();
            if(firstLineString.contains("view contents") || firstLineString.contains("Selling for: ")) {
                translateOperationSection(seg);
            }
            else if(firstLineString.contains("Your pouch is empty")) {
                resultText.append(Text.translatable("wytr.tooltip.ingredientPouch.empty").setStyle(GRAY));
                break;
            }
            else {
                translateContentSection(seg);
                break;
            }
            resultText.append(SPLITTER);
        }
    }

    private void translateNameSection(List<Text> segment) {
        String string = segment.getFirst().getString();
        if(string.equals("§6Ingredient Pouch")) {
            resultText.append(Text.translatable("wytr.tooltip.ingredientPouch").setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
        }
        else if(string.equals("§cClick to confirm")) {
            resultText.append(Text.translatable("wytr.tooltip.ingredientPouch.sellMode").setStyle(Style.EMPTY.withColor(Formatting.RED)));
        }
        else {
            segment.forEach(resultText::append);
        }
    }

    private void translateOperationSection(List<Text> segment) {
        for(Text t : segment) {
            String string = t.getString();
            if(string.contains("view contents")) {
                resultText.append(Text.translatable("wytr.tooltip.ingredientPouch.view"));
            }
            else if(string.contains("to sell ")) {
                String price = "§7" + string.replaceFirst(".+ to sell ", "");
                resultText.append(Text.translatable("wytr.tooltip.ingredientPouch.sell", price));
            }
            else if(string.contains("Selling for: ")) {
                String price = string.replaceFirst("§7Selling for: ", "");
                resultText.append(Text.translatable("wytr.tooltip.ingredientPouch.price", price).setStyle(GRAY));
            }
            else {
                resultText.append(t);
            }
        }
    }

    private void translateContentSection(List<Text> segment) {
        colorCodedToStyledBatch(segment).forEach(t -> {
            Deque<Text> mutableSibling = new ArrayDeque<>(t.getSiblings());
            MutableText result = Text.empty();
            result.append(mutableSibling.removeFirst());
            result.append(new ItemName(mutableSibling.removeFirst()).textAsMutable().setStyle(GRAY));
            mutableSibling.forEach(result::append);
            resultText.append(result);
        });
    }
}
