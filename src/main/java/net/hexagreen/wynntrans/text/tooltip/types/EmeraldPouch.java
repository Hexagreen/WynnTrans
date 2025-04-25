package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.IPricedItem;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmeraldPouch extends WynnTooltipText implements IPricedItem, ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("§aEmerald Pouch§2 \\[Tier [IVX]+].*");
    }

    public EmeraldPouch(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "tooltip.emeraldPouch";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        ItemTooltipPriceSection priceSection = detachPriceSection(segments);
        int priceSectionIndex = priceSection.position();

        for(int i = 0, size = segments.size(); i < size; i++) {
            if(i == 0) {
                translateNameSection(segments.getFirst());
                resultText.append(SPLITTER);
                continue;
            }

            if(i == priceSectionIndex) {
                priceSection.priceTexts().forEach(resultText::append);
                resultText.append(SPLITTER);
            }

            List<Text> seg = segments.get(i);
            String firstLine = seg.getFirst().getString();
            if(firstLine.equals("Emerald Pouches allows the wearer")) {
                translateInfoSection(seg);
            }
            else if(firstLine.matches("^- .+")) {
                translateSpecSection(seg);
            }
            else if(firstLine.equals("Right-Click to view contents")) {
                translateActionSection(seg);
            }
            else {
                seg.forEach(resultText::append);
            }
            if(i != size - 1) resultText.append(SPLITTER);
        }
    }

    private void translateNameSection(List<Text> segment) {
        String tier = "§2" + segment.getFirst().getString().replaceFirst(".+ \\[Tier ([IVX]+)].*", "$1");
        Text name = Text.translatable(translationKey, tier).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
        resultText.append(name);

        Text _count;
        if(segment.size() == 2) _count = segment.get(1);
        else return;
        if(_count.getString().equals("Empty")) {
            resultText.append(Text.translatable(translationKey + ".empty").setStyle(GRAY));
        }
        else {
            resultText.append(_count);
        }
    }

    private void translateInfoSection(List<Text> ignoredSegment) {
        wrapLine(Text.translatable(translationKey + ".info").setStyle(GRAY), 150).forEach(resultText::append);
    }

    private void translateSpecSection(List<Text> segment) {
        for(Text _line : segment) {
            Matcher numFinder = Pattern.compile("\\d+").matcher(_line.getString());
            List<String> numbers = new ArrayList<>();
            while(numFinder.find()) {
                numbers.add(numbers.isEmpty() ? numFinder.group() : "§8" + numFinder.group());
            }
            String colorCoded = styleToColorCode(_line.getSiblings(), GRAY).getString();
            String v = colorCoded.replaceAll("(?<!§)\\d+", "%s");
            String k = translationKey + ".spec." + DigestUtils.sha1Hex(v).substring(0, 4);
            WTS.checkTranslationExist(k, v);
            resultText.append(Text.translatableWithFallback(k, colorCoded, numbers.toArray(Object[]::new)).setStyle(GRAY));
        }
    }

    private void translateActionSection(List<Text> ignoredSegment) {
        resultText.append(Text.translatable(translationKey + ".view"));
    }
}
