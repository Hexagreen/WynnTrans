package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.chat.types.SyndicatePromotion;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AbilityTreeAspectSocket extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!Pattern.compile("\uE002").matcher(WynnTrans.currentScreen).find()) return false;
        return texts.getFirst().getString().matches("(Empty|Locked) Aspect Socket");
    }

    public AbilityTreeAspectSocket(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "tooltip.abilityTree.aspectSocket";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> dump = new ArrayList<>();
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);

        Style DARK_GRAY = Style.EMPTY.withColor(Formatting.DARK_GRAY);
        if(getSiblings().getFirst().getString().contains("Locked ")) {
            dump.add(Text.translatable(translationKey + ".locked").setStyle(DARK_GRAY.withBold(true)));
            dump.add(SPLITTER);
            dump.addAll(wrapLine(Text.translatable(translationKey + ".locked.desc").setStyle(DARK_GRAY), 150));
            dump.add(SPLITTER);
        }
        else {
            dump.add(Text.translatable(translationKey + ".empty").setStyle(DARK_GRAY.withBold(true)));
            dump.add(SPLITTER);
            dump.addAll(wrapLine(Text.translatable(translationKey + ".empty.desc").setStyle(DARK_GRAY), 150));
            dump.add(SPLITTER);
        }

        if(segments.getLast().getFirst().getString().matches("^[✔✖] .+")) {
            segments.getLast().forEach(t -> {
                List<Text> siblings = t.getSiblings();
                MutableText translated = Text.empty().append(siblings.getFirst());
                switch(siblings.get(1).getString()) {
                    case "Combat Lv.: " -> {
                        translated.append(Text.translatable(translationKey + ".reqCombat").setStyle(GRAY));
                        translated.append(siblings.get(2));
                        dump.add(translated);
                    }
                    case "Min. Raiding Level: " -> {
                        translated.append(Text.translatable(translationKey + ".reqRaid").setStyle(GRAY));
                        translated.append(SyndicatePromotion.getTranslatedRankName("§f" + siblings.get(2).getString()));
                        dump.add(translated);
                    }
                    default -> dump.add(t);
                }
            });
            dump.add(SPLITTER);
        }
        dump.removeLast();

        dump.forEach(resultText::append);
    }
}
