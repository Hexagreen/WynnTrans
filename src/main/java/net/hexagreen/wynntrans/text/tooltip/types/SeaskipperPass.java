package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;
import java.util.Locale;

public class SeaskipperPass extends WynnTooltipText {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches(".+ Pass §7for §b\\d+²");
    }

    public SeaskipperPass(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        String _destination = getSibling(-1).getSiblings().getLast().getString();
        Text cost = getSibling(0).getSiblings().getLast();
        Text bar = getSibling(1).getSiblings().getFirst();
        Text level = getSibling(1).getSiblings().getLast();
        String _status = getSibling(2).getSiblings().getLast().getString();
        Text coordinate = getSibling(3).getSiblings().getLast();

        Text destination = Text.translatableWithFallback("wytr.area." + normalizeStringForKey(_destination), _destination)
                .setStyle(getSibling(-1).getSiblings().getLast().getStyle());
        Text ticket = Text.translatable("wytr.tooltip.seaskipper.pass", destination)
                .setStyle(getSibling(0).getSiblings().getFirst().getStyle());
        Text status = Text.translatable("wytr.tooltip.seaskipper.status." + _status.toLowerCase(Locale.ENGLISH))
                .setStyle(getSibling(2).getSiblings().getLast().getStyle());

        resultText.append(Text.translatable("wytr.tooltip.seaskipper.ticketPrice", ticket, cost).formatted(Formatting.GRAY));
        resultText.append(Text.empty().append(bar).append(Text.translatable("wytr.tooltip.seaskipper.level", level).formatted(Formatting.GRAY)));
        resultText.append(Text.empty().append(bar).append(Text.translatable("wytr.tooltip.seaskipper.status", status).formatted(Formatting.GRAY)));
        resultText.append(Text.empty().append(bar).append(Text.translatable("wytr.tooltip.seaskipper.coord", coordinate).formatted(Formatting.GRAY)));
        resultText.append(" ");
        resultText.append(Text.translatable("wytr.tooltip.seaskipper.click", destination).formatted(Formatting.GRAY));
    }
}
