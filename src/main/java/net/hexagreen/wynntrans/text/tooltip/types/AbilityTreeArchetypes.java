package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AbilityTreeArchetypes extends WynnTooltipText implements ITooltipSplitter {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!Pattern.compile("[\uE000\uE001]").matcher(WynnTrans.currentScreen).find()) return false;
        return texts.getFirst().getString().matches(".+ Archetype") && texts.getLast().getString().matches(".+§7Unlocked Abilities:.+");
    }

    public AbilityTreeArchetypes(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> dump = new ArrayList<>();
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);

        Text _archetype = getSibling(0).getSiblings().getFirst();
        String v = _archetype.getString().replaceFirst(" Archetype", "");
        String k = "wytr.ability.archetype." + normalizeStringForKey(v);
        Text archetype = Text.translatableWithFallback(k, v);
        dump.add(Text.translatable("wytr.tooltip.abilityNode.archetype", archetype).setStyle(_archetype.getStyle()));
        dump.add(SPLITTER);

        Text _archetypeDesc = styleToColorCode(mergeTextStyleSide(segments.get(1)).getSiblings(), GRAY);
        String k1 = k + ".desc";
        String v1 = _archetypeDesc.getString();
        WTS.checkTranslationExist(k1, v1);
        dump.addAll(wrapLine(Text.translatableWithFallback(k1, v1).setStyle(GRAY), 150));
        dump.add(SPLITTER);

        List<Text> _unlockCounter = getSibling(-1).getSiblings();
        Text unlockCounter = Text.empty().append(_unlockCounter.getFirst())
                .append(Text.translatable("wytr.tooltip.abilityTree.archetypeCounter").setStyle(GRAY))
                .append(_unlockCounter.get(2))
                .append(_unlockCounter.getLast());
        dump.add(unlockCounter);

        dump.forEach(resultText::append);
    }
}
