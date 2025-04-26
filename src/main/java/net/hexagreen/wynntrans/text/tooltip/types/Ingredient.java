package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.tooltip.IPricedItem;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Ingredient extends WynnTooltipText implements IPricedItem, ITooltipSplitter {
    private final List<Text> tempText;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 6) return false;
        return texts.get(1).getString().equals("§8Crafting Ingredient") || texts.get(5).getString().equals("§7§8Crafting Ingredient");
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString().replaceFirst(" \\[✫✫✫]", "");
        if(!WTS.checkTranslationDoNotRegister("wytr.ingredient." + normalizeStringForKey(_iName))) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        Ingredient ingredient = new Ingredient(textBowl);
        ingredient.translateNameSection(ingredient.inputText.getSiblings());
        return ingredient.tempText.getFirst().copy();
    }

    public Ingredient(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
        this.tempText = new ArrayList<>();
    }

    @Override
    protected String setTranslationKey() {
        return "";
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
                tempText.add(SPLITTER);
                continue;
            }
            else if(i == priceSectionIndex) {
                tempText.addAll(priceSection.priceTexts());
                tempText.add(SPLITTER);
            }

            List<Text> seg = segments.get(i);
            String firstLineString = seg.getFirst().getString();
            if(firstLineString.equals("Crafting Ingredient")) {
                translateNameSection(seg);
            }
            else if(firstLineString.matches("[+-]\\d+% Ingredient Effectiveness")) {
                translateEffectiveness(seg);
            }
            else if(firstLineString.matches("[+-]\\d+s? (Durability|Duration|Charges?|.+ Min\\.).*")) {
                translateSideEffect(seg);
            }
            else if(firstLineString.matches("([+-]?\\d+ to )?[+-]?\\d+(%|/[35]s| tier)? .+")) {
                translateIDSection(seg);
            }
            else if(firstLineString.matches("[✔✖] Crafting Lv\\. Min: \\d+")) {
                translateTargetProfession(seg);
                break;
            }
            tempText.add(SPLITTER);
        }

        tempText.forEach(resultText::append);
    }

    private void translateNameSection(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        for(Text t : segment) {
            if(t.getString().contains("[✫✫✫]")) {
                Deque<Text> mutableSiblings = new ArrayDeque<>(t.getSiblings());
                String _ingredientName = mutableSiblings.removeFirst().getString();
                boolean trailingBracket = _ingredientName.contains(" [");
                Text ingredientName = new ItemName(_ingredientName.replaceFirst(" \\[$", "")).textAsMutable().setStyle(GRAY);
                MutableText nameLine = Text.empty().append(ingredientName);
                if(trailingBracket) nameLine.append(Text.literal(" [").setStyle(GRAY));
                mutableSiblings.forEach(nameLine::append);
                dump.add(nameLine);
            }
            else if(t.getString().equals("Crafting Ingredient")) {
                dump.add(Text.translatable("wytr.tooltip.craftingIngredient").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
            else {
                dump.add(t);
            }
        }

        tempText.addAll(dump);
    }

    private void translateIDSection(List<Text> segment) {
        List<Text> translatedIDs = new IdentificationTooltip(colorCodedToStyledBatch(segment)).text();
        tempText.addAll(translatedIDs);
    }

    private void translateEffectiveness(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        for(Text t : segment) {
            List<Text> siblings = t.getSiblings();
            if(siblings.size() == 2) {
                Text body = Text.translatable("wytr.id.IngredientEffectiveness").setStyle(GRAY);
                dump.add(Text.empty().append(siblings.getFirst()).append(body));
            }
            else {
                String v = t.getString().replaceAll("[()]", "");
                String k = "wytr.tooltip.ingredient.effectiveness." + DigestUtils.sha1Hex(v).substring(0, 4);
                WTS.checkTranslationExist(k, v);
                dump.add(Text.empty().setStyle(GRAY).append("(").append(Text.translatableWithFallback(k, v)).append(")"));
            }
        }

        tempText.addAll(dump);
    }

    private void translateSideEffect(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        for(Text t : segment) {
            List<Text> siblings = t.getSiblings();
            String string = t.getString();
            if(siblings.size() == 3) {
                dump.add(Text.translatable("wytr.tooltip.ingredient.selective",
                        getDurabilityOrDuration(siblings.getFirst()),
                        getDurabilityOrDuration(siblings.getLast())).setStyle(GRAY));
            }
            else if(string.contains("Dura")) {
                dump.add(getDurabilityOrDuration(siblings.getFirst()));
            }
            else if(string.contains("Charge")) {
                Text charge = siblings.getFirst();
                String num = charge.getString().split(" ", 2)[0];
                dump.add(Text.translatable("wytr.tooltip.ingredient.charge", num).setStyle(charge.getStyle()));
            }
            else if(string.contains("Min.")) {
                Text req = siblings.getFirst();
                String[] slices = req.getString().split(" ");
                String num = slices[0];
                Text skill = CharacterSkill.getSkill(slices[1]).setStyle(req.getStyle());
                dump.add(Text.translatable("wytr.tooltip.ingredient.requirement", num, skill).setStyle(req.getStyle()));
            }
            else {
                dump.add(t);
            }
        }
        tempText.addAll(dump);
    }

    private Text getDurabilityOrDuration(Text text) {
        Style style = text.getStyle();
        String string = text.getString();
        if(string.contains("Durability")) {
            String num = string.split(" ", 2)[0];
            return Text.translatable("wytr.tooltip.ingredient.durability", num).setStyle(style);
        }
        else if(string.contains("Duration")) {
            String sign = string.substring(0, 1);
            Text num = Text.literal(sign)
                    .append(ITime.translateTime(string.replaceFirst("[+-]", "").split(" ", 2)[0]));
            return Text.translatable("wytr.tooltip.ingredient.duration", num).setStyle(style);
        }
        else return text;
    }

    private void translateTargetProfession(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        for(int i = 0, size = segment.size(); i < size; i++) {
            List<Text> siblings = segment.get(i).getSiblings();
            if(i == 0) {
                String level = siblings.getLast().getString().replaceAll("\\D", "");
                dump.add(Text.empty().append(siblings.getFirst()).append(" ")
                        .append(Text.translatable("wytr.tooltip.ingredient.craftingLevel", level).setStyle(GRAY)));
            }
            else {
                dump.add(Text.empty().append(siblings.getFirst()).append(siblings.get(1))
                        .append(Profession.getProfession(siblings.get(2).getString().charAt(0)).getTextWithIcon().setStyle(GRAY)));
            }
        }
        tempText.addAll(dump);
    }
}
