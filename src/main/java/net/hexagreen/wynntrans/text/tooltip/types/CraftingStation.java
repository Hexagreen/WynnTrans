package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.enums.EquipmentAndConsumables;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CraftingStation extends WynnTooltipText implements ITooltipSplitter, ISpaceProvider {
    private static final Pattern RECIPE_REGEX = Pattern.compile("([✔✖] )Lv\\. (\\d+(?:\\.\\d+)?): ");

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!WynnTrans.currentScreen.matches("(Scribing|Jeweling|Alchemism|Cooking|Weaponsmithing|Tailoring|Woodworking|Armouring)"))
            return false;
        return Items.match(texts.getFirst().getString()) != null;
    }

    public CraftingStation(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "tooltip.craftingStation";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Items match = Items.match(getSibling(0).getString());
        switch(match) {
            case ADJUST_LEVEL -> buildAdjustLevel();
            case NO_RECIPE -> buildNoRecipe();
            case INGREDIENT_ERROR -> buildIngredientError();
            case CRAFT -> buildCraft();
            case IN_CRAFTING -> buildInCrafting();
            case NOT_ENOUGH_ITEM -> buildNEI();
            case RESULT_SLOT -> buildResultSlot();
        }
    }

    private void buildAdjustLevel() {
        List<Text> firstLine = getSibling(0).getSiblings();
        String key = firstLine.getFirst().getString().contains("Increase") ? translationKey + ".incLevel" : translationKey + ".decLevel";
        Text recipe = EquipmentAndConsumables.getText(firstLine.get(1).getString()).formatted(Formatting.YELLOW);
        String[] levels = firstLine.getLast().getString().replaceAll("\\[Lv\\. |]", "").split(" to ");
        Text levelRange = Text.translatable("wytr.unit.levelRange", levels[0], levels[1]).formatted(Formatting.DARK_GRAY);
        resultText.append(Text.translatable(key, recipe, levelRange).formatted(Formatting.GOLD));
        for(Text t : getSiblings().subList(1, getSiblings().size())) {
            List<Text> siblings = t.getSiblings();
            Text icon = siblings.getFirst();
            String[] levels_ = siblings.getLast().getString().replaceAll(" |Lv\\.", "").split("to");
            Text levelRange_ = Text.translatable("wytr.unit.levelRange", levels_[0], levels_[1]).setStyle(siblings.getLast().getStyle());
            resultText.append(Text.empty().append(icon).append(" ").append(levelRange_));
        }
    }

    private void buildNoRecipe() {
        List<Deque<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments, ArrayDeque::new);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            Deque<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.equals("Incomplete Recipe")) {
                resultText.append(Text.translatable(translationKey + ".noRecipe").formatted(Formatting.RED, Formatting.BOLD));
            }
            else if(_string.contains(" Recipes:")) {
                String _recipes = seg.removeFirst().getString().replaceAll(" Recipes:", "");
                Text recipe = EquipmentAndConsumables.getText(_recipes);
                resultText.append(Text.translatable(translationKey + ".noRecipe.sample", recipe).formatted(Formatting.GOLD, Formatting.BOLD));
                for(Text line : seg) {
                    List<Text> siblings = line.getSiblings();
                    String _level = siblings.getFirst().getString();
                    Text levelOrIndent;
                    if(!_level.isBlank()) {
                        Matcher m = RECIPE_REGEX.matcher(_level);
                        boolean ignore = m.find();
                        levelOrIndent = Text.empty().setStyle(siblings.getFirst().getStyle())
                                .append(m.group(1))
                                .append(Text.translatable("wytr.unit.level", m.group(2)))
                                .append(": ");
                    }
                    else {
                        levelOrIndent = getBlanks(resultText.getSiblings().getLast().getSiblings().getFirst());
                    }
                    Text amount = siblings.get(1);
                    Text material = new ItemName(siblings.get(2)).textAsMutable();
                    Text icon = siblings.getLast();
                    resultText.append(Text.empty().append(levelOrIndent).append(amount).append(" ").append(material).append(icon));
                }
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildIngredientError() {
        resultText.append(Text.translatable(translationKey + ".invalidIngredient").formatted(Formatting.RED, Formatting.BOLD));
        wrapLine(Text.translatable(translationKey + ".invalidIngredient.desc").formatted(Formatting.RED), 150)
                .forEach(resultText::append);
    }

    private void buildCraft() {
        List<Deque<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments, ArrayDeque::new);
        for(int i = 0, segmentsSize = segments.size(); i < segmentsSize; i++) {
            Deque<Text> seg = segments.get(i);
            String _string = seg.getFirst().getString();
            if(_string.matches(". Craft .")) {
                Text icon = seg.getFirst().getSiblings().getFirst();
                resultText.append(Text.translatable(translationKey + ".craft", icon).formatted(Formatting.BOLD));
                resultText.append(Text.translatable(translationKey + ".craft.once"));
                List<Text> lastLineSib = seg.getLast().getSiblings();
                Text bar = lastLineSib.getFirst();
                Text recipe = EquipmentAndConsumables.getText(lastLineSib.get(1).getString()).formatted(Formatting.WHITE);
                String[] levels = lastLineSib.getLast().getString().replaceAll("\\[Lv\\. |]", "").split(" to ");
                Text level = Text.empty().formatted(Formatting.GRAY)
                        .append("[").append(Text.translatable("wytr.unit.levelRange", levels[0], levels[1])).append("]");
                resultText.append(Text.empty().append(bar).append(recipe).append(" ").append(level));
            }
            else if(_string.contains("Crafting Time:")) {
                Text time = ITime.translateTime(seg.getFirst().getSiblings().getLast());
                resultText.append(Text.translatable(translationKey + ".craft.time", time).formatted(Formatting.GRAY));
            }
            else if(_string.contains("non-stop")) {
                resultText.append(Text.translatable(translationKey + ".craft.bulk"));
            }
            else seg.forEach(resultText::append);
            if(i != segmentsSize - 1) resultText.append(SPLITTER);
        }
    }

    private void buildInCrafting() {
        resultText.append(Text.translatable(translationKey + ".inCrafting").formatted(Formatting.RED));
        resultText.append(SPLITTER);
        resultText.append(Text.translatable("wytr.func.clickToCancel").formatted(Formatting.RED));
    }

    private void buildNEI() {
        resultText.append(Text.translatable(translationKey + ".notEnough").formatted(Formatting.RED, Formatting.BOLD));
        wrapLine(Text.translatable(translationKey + ".notEnough.desc").formatted(Formatting.RED), 150)
                .forEach(resultText::append);
    }

    private void buildResultSlot() {
        resultText.append(Text.translatable(translationKey + ".resultSlot").formatted(Formatting.GRAY));
        wrapLine(Text.translatable(translationKey + ".resultSlot.desc").formatted(Formatting.DARK_GRAY), 150)
                .forEach(resultText::append);
    }

    private enum Items {
        ADJUST_LEVEL("(In|De)crease .+ Level \\[Lv\\. \\d+ to \\d+]"),
        NO_RECIPE("Incomplete Recipe"),
        INGREDIENT_ERROR("Invalid Ingredients"),
        CRAFT("[Ⓐ-Ⓛ] Craft [Ⓐ-Ⓛ]"),
        IN_CRAFTING("Crafting..."),
        NOT_ENOUGH_ITEM("Not enough items"),
        RESULT_SLOT("Crafted Item Slot");

        private final Pattern regex;

        static Items match(String string) {
            String finalString = string.replaceAll("§.", "");
            return Arrays.stream(values())
                    .filter(i -> i.regex.matcher(finalString).matches())
                    .findAny().orElse(null);
        }

        Items(String regex) {
            this.regex = Pattern.compile(regex);
        }
    }
}
