package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.enums.CharacterClass;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AbilityTreeSelector extends WynnTooltipText implements ITooltipSplitter {
    private static final Text SELECTED =
            Text.translatable("wytr.func.currentSelection").setStyle(Style.EMPTY.withColor(Formatting.YELLOW));
    private static final Text SELECTABLE =
            Text.empty().append(Text.literal("\uE000").setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:keybind"))))
                    .append(" ")
                    .append(Text.translatable("wytr.func.clickToOpen").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!Pattern.compile("[\uE000\uE001\uE002]").matcher(WynnTrans.currentScreen).find()) return false;
        return texts.getLast().getString().matches("§7Currently Selected|§7\uE000 Click to Open");
    }

    public AbilityTreeSelector(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "tooltip.abilityTree";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> dump = new ArrayList<>();
        List<Text> siblings = getSiblings();

        String head = siblings.getFirst().getString();
        Style headStyle = siblings.getFirst().getSiblings().getFirst().getStyle();
        if(head.equals("Aspects")) {
            dump.add(Text.translatable(translationKey + ".aspects").setStyle(headStyle));
            dump.add(SPLITTER);
            dump.addAll(wrapLine(Text.translatable(translationKey + ".aspects.desc").setStyle(GRAY), 150));
        }
        else if(head.equals("Reset your Tree")) {
            dump.add(Text.translatable(translationKey + ".resetTree").setStyle(headStyle));
            dump.add(SPLITTER);
            dump.addAll(wrapLine(Text.translatable(translationKey + ".resetTree.desc").setStyle(GRAY), 150));
        }
        else if(head.matches(".+ Abilities")) {
            Text className = CharacterClass.getClassName(head.replaceFirst(" Abilities", ""));
            dump.add(Text.translatable(translationKey + ".abilities", className).setStyle(headStyle));
            dump.add(SPLITTER);
            dump.addAll(wrapLine(Text.translatable(translationKey + ".abilities.desc").setStyle(GRAY), 150));
        }
        else {
            resultText = inputText;
            return;
        }

        dump.add(SPLITTER);
        if(siblings.getLast().getString().equals("Currently Selected")) {
            dump.add(SELECTED);
        }
        else {
            dump.add(SELECTABLE);
        }
        
        dump.forEach(resultText::append);
    }
}
