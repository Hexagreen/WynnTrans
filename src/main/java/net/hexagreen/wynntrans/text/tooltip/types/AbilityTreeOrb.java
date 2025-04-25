package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AbilityTreeOrb extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!Pattern.compile("[\uE000\uE001]").matcher(WynnTrans.currentScreen).find()) return false;
        return texts.getFirst().getString().matches("(§e§l)?Ability Points|Ability Tree Reset");
    }

    public AbilityTreeOrb(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "tooltip.abilityPoint";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        List<Text> dump = new ArrayList<>();

        segments.forEach(seg -> {
            List<Text> firstTextSiblings = seg.getFirst().getSiblings();
            Text rep = firstTextSiblings.getFirst();
            Style repStyle = rep.getStyle();
            if(rep.getString().equals("Ability Points")) {
                dump.add(Text.translatable(translationKey).setStyle(repStyle));
            }
            else if(rep.getString().equals("Ability Tree Reset")) {
                dump.add(Text.translatable(translationKey + ".reset").setStyle(repStyle));
            }
            else if(rep.getString().equals("✦ Available Points: ")) {
                dump.add(Text.translatable(translationKey + ".available", firstTextSiblings.get(1), firstTextSiblings.get(2))
                        .setStyle(repStyle));
            }
            else if(rep.getString().equals("Next Ability Points:")) {
                seg.forEach(t -> {
                    String string = t.getString();
                    if(string.matches("Next Ability Points:")) {
                        dump.add(Text.translatable(translationKey + ".next").setStyle(repStyle));
                    }
                    else if(string.matches("^- .+")) {
                        String num = string.replaceAll("\\D", "");
                        dump.add(Text.translatable(translationKey + ".next.level", num).setStyle(GRAY));
                    }
                    else if(string.matches("  ÀÀ\\.\\.\\.")) {
                        dump.add(Text.literal("  ÀÀ...").setStyle(GRAY));
                    }
                    else {
                        dump.add(t);
                    }
                });
            }
            else if(rep.getString().equals("Shift Click to reset your tree")) {
                dump.add(Text.translatable(translationKey + ".shiftToReset").setStyle(repStyle));
            }
            else if(rep.getString().equals("Ability Points are used")) {
                Text translated = Text.translatable(translationKey + ".info").setStyle(repStyle);
                dump.addAll(wrapLine(translated, 150));
            }
            else if(rep.getString().equals("You are free to edit your")) {
                Text translated = Text.translatable(translationKey + ".info.editMode").setStyle(repStyle);
                dump.addAll(wrapLine(translated, 150));
            }
            else if(rep.getString().equals("Reset your Ability Tree and")) {
                Text translated = Text.translatable(translationKey + ".info.resetMode").setStyle(repStyle);
                dump.addAll(wrapLine(translated, 150));
            }
            else {
                dump.addAll(seg);
            }
            dump.add(SPLITTER);
        });
        dump.removeLast();

        dump.forEach(resultText::append);
    }
}
