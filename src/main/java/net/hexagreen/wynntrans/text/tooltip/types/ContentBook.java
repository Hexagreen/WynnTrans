package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class ContentBook extends WynnTooltipText {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 9) return false;
        return texts.getFirst().getString().equals("§7Content Book");
    }

    public ContentBook(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        for(int i = 0, size = getSiblings().size(); i < size; i++) {
            Style style = getSibling(i).getSiblings().getFirst().getStyle();
            List<Text> siblings = getSibling(i).getSiblings();
            Text t = switch(i) {
                case 0 -> Text.translatable("wytr.tooltip.contentBook").setStyle(style);
                case 1 -> Text.translatable("wytr.tooltip.contentBook.desc").setStyle(style);
                case 2, 4 -> getSibling(i);
                case 3 ->
                        Text.translatable("wytr.tooltip.contentBook.quest", siblings.get(1), siblings.get(2)).setStyle(style);
                case 5 -> Text.translatable("wytr.tooltip.contentBook.discovery").setStyle(style);
                case 6 -> Text.empty().append(siblings.getFirst())
                        .append(Text.translatable("wytr.tooltip.contentBook.territorial", siblings.get(2), siblings.get(3)).setStyle(siblings.get(1).getStyle()));
                case 7 -> Text.empty().append(siblings.getFirst())
                        .append(Text.translatable("wytr.tooltip.contentBook.world", siblings.get(2), siblings.get(3)).setStyle(siblings.get(1).getStyle()));
                case 8 -> Text.empty().append(siblings.getFirst())
                        .append(Text.translatable("wytr.tooltip.contentBook.secret", siblings.get(2), siblings.get(3)).setStyle(siblings.get(1).getStyle()));
                default -> null;
            };
            if(t != null) resultText.append(t);
        }
    }
}
