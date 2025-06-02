package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class Compass extends WynnTooltipText {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 10) return false;
        return texts.getFirst().getString().equals("§bCharacter Info");
    }

    public Compass(List<Text> texts) {
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
            Text t = switch(i) {
                case 0 -> Text.translatable("wytr.tooltip.compassMenu").setStyle(style);
                case 1, 5, 8 -> getSibling(i);
                case 2 -> {
                    List<Text> lines = wrapLine(Text.translatable("wytr.tooltip.compassMenu.desc").setStyle(GRAY), 200);
                    lines.forEach(resultText::append);
                    yield null;
                }
                case 6 ->
                        Text.translatable("wytr.tooltip.compassMenu.unusedSP", getSibling(i).getSiblings().getLast()).setStyle(style);
                case 7 ->
                        Text.translatable("wytr.tooltip.compassMenu.unusedAP", getSibling(i).getSiblings().getLast()).setStyle(style);
                case 9 -> {
                    MutableText line = Text.empty().append(getSibling(i).getSiblings().getFirst()).append(" ");
                    yield line.append(Text.translatable("wytr.func.clickToOpen").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
                }
                default -> null;
            };
            if(t != null) resultText.append(t);
        }
    }
}
