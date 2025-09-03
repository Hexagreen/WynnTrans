package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.chat.Rulebooks;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class SimpleItemTooltip extends WynnTooltipText {
    private final Rulebooks.ItemTooltipKeyBook.ItemTooltipTranslationKey template;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        String itemName = texts.getFirst().getString();
        if(WTS.getRulebooks().itemTooltipKeyBook.findItem(itemName) != null) return true;
        for(int i = texts.size() - 1; i > 0; i--) {
            String line = texts.get(i).getString();
            if(line.replaceFirst("(§.)+", "").matches("Quest Item|Untradable Item|Misc\\. Item")) return true;
        }
        return WTS.checkTranslationDoNotRegister("wytr.item.misc." + normalizeStringForKey(itemName));
    }

    public SimpleItemTooltip(List<Text> texts) {
        super(texts);
        this.template = WTS.getRulebooks().itemTooltipKeyBook.findItem(texts.getFirst().getString());
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Rulebooks.ItemTooltipKeyBook.ItemTooltipLine> lines =
                template != null ? new ArrayList<>(template.lines()) : List.of();
        for(int i = 0, size = getSiblings().size(); i < size; i++) {
            String text = getSibling(i).getString();
            Style style = getSibling(i).getSiblings().isEmpty() ? getStyle(i) : getSibling(i).getSiblings().getFirst().getStyle();
            if(i == 0) {
                String key;
                if(template != null) key = template.key();
                else key = "wytr.item.misc." + normalizeStringForKey(text);
                if(WTS.checkTranslationDoNotRegister(key)) resultText.append(Text.translatable(key).setStyle(style));
                else resultText.append(getSibling(i));
            }
            else if(text.equals("Untradable Item")) {
                resultText.append(Text.translatable("wytr.tooltip.item.untradable").setStyle(style));
            }
            else if(text.equals("Quest Item")) {
                resultText.append(Text.translatable("wytr.tooltip.item.questItem").setStyle(style));
            }
            else if(text.equals("Misc. Item")) {
                resultText.append(Text.translatable("wytr.tooltip.item.miscItem").setStyle(style));
            }
            else {
                if(template != null) {
                    if(i == lines.getFirst().lineNumber()) {
                        wrapLine(Text.translatable(lines.removeFirst().key()).setStyle(style), 150).forEach(resultText::append);
                        break;
                    }
                }
                else resultText.append(getSibling(i));
            }
        }
    }
}
