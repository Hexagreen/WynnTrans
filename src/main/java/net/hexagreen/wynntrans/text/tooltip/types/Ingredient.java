package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
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

public class Ingredient extends WynnTooltipText implements ITradableItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 6) return false;
        return texts.get(1).getString().equals("§8Crafting Ingredient") || texts.get(5).getString().equals("§7§8Crafting Ingredient");
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString().replaceFirst(" \\[✫✫✫]", "");
        if(!WTS.checkTranslationDoNotRegister("wytr.item.ingredient." + normalizeStringForKey(_iName))) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        Ingredient ingredient = new Ingredient(textBowl);
        ingredient.translateNameSection(ingredient.inputText.getSiblings());
        return ingredient.resultText.getSiblings().getFirst().copy();
    }

    public Ingredient(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        insertPriceSection(segments, resultText::append, (seg, i) -> {
            String firstLine = seg.getFirst().getString();
            if(i == 0) {
                translateNameSection(seg);
            }
            else if(firstLine.equals("Crafting Ingredient")) {
                translateNameSection(seg);
            }
            else if(firstLine.matches("[+-]\\d+% Ingredient Effectiveness")) {
                translateEffectiveness(seg);
            }
            else if(firstLine.matches("[+-]\\d+s? (Durability|Duration|Charges?|.+ Min\\.).*")) {
                translateSideEffect(seg);
            }
            else if(firstLine.matches("([+-]?\\d+ to )?[+-]?\\d+(%|/[35]s| tier)? .+")) {
                translateIDSection(seg);
            }
            else if(firstLine.matches("[✔✖] Crafting Lv\\. Min: \\d+")) {
                translateTargetProfession(seg);
            }
            if(i != segments.size() - 1) resultText.append(SPLITTER);
        });
    }

    private void translateNameSection(List<Text> segment) {
        for(Text t : segment) {
            if(t.getString().contains("[✫✫✫]")) {
                Deque<Text> mutableSiblings = new ArrayDeque<>(t.getSiblings());
                String _ingredientName = mutableSiblings.removeFirst().getString();
                boolean trailingBracket = _ingredientName.contains(" [");
                Text ingredientName = new ItemName(_ingredientName.replaceFirst(" \\[$", "")).textAsMutable().setStyle(GRAY);
                MutableText nameLine = Text.empty().append(ingredientName);
                if(trailingBracket) nameLine.append(Text.literal(" [").setStyle(GRAY));
                mutableSiblings.forEach(nameLine::append);
                resultText.append(nameLine);
            }
            else if(t.getString().equals("Crafting Ingredient")) {
                resultText.append(Text.translatable("wytr.tooltip.ingredient").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
            else {
                resultText.append(t);
            }
        }
    }

    private void translateIDSection(List<Text> segment) {
        new IdentificationTooltip(colorCodedToStyledBatch(segment)).text().forEach(resultText::append);
    }

    private void translateEffectiveness(List<Text> segment) {
        for(Text t : segment) {
            List<Text> siblings = t.getSiblings();
            if(siblings.size() == 2) {
                Text body = Text.translatable("wytr.id.IngredientEffectiveness").setStyle(GRAY);
                resultText.append(Text.empty().append(siblings.getFirst()).append(body));
            }
            else {
                String v = t.getString().replaceAll("[()]", "");
                String k = "wytr.tooltip.ingredient.effectiveness." + DigestUtils.sha1Hex(v).substring(0, 4);
                WTS.checkTranslationExist(k, v);
                resultText.append(Text.empty().setStyle(GRAY).append("(").append(Text.translatableWithFallback(k, v)).append(")"));
            }
        }
    }

    private void translateSideEffect(List<Text> segment) {
        for(Text t : segment) {
            List<Text> siblings = t.getSiblings();
            String string = t.getString();
            if(siblings.size() == 3) {
                resultText.append(Text.translatable("wytr.tooltip.ingredient.selective",
                        getDurabilityOrDuration(siblings.getFirst()),
                        getDurabilityOrDuration(siblings.getLast())).setStyle(GRAY));
            }
            else if(string.contains("Dura")) {
                resultText.append(getDurabilityOrDuration(siblings.getFirst()));
            }
            else if(string.contains("Charge")) {
                Text charge = siblings.getFirst();
                String num = charge.getString().split(" ", 2)[0];
                resultText.append(Text.translatable("wytr.tooltip.ingredient.charge", num).setStyle(charge.getStyle()));
            }
            else if(string.contains("Min.")) {
                Text req = siblings.getFirst();
                String[] slices = req.getString().split(" ");
                String num = slices[0];
                Text skill = CharacterSkill.getSkill(slices[1]).getStat().setStyle(req.getStyle());
                resultText.append(Text.translatable("wytr.tooltip.ingredient.requirement", num, skill).setStyle(req.getStyle()));
            }
            else {
                resultText.append(t);
            }
        }
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
        for(int i = 0, size = segment.size(); i < size; i++) {
            List<Text> siblings = segment.get(i).getSiblings();
            if(i == 0) {
                String level = siblings.getLast().getString().replaceAll("\\D", "");
                resultText.append(Text.empty().append(siblings.getFirst()).append(" ")
                        .append(Text.translatable("wytr.tooltip.ingredient.craftingLevel", level).setStyle(GRAY)));
            }
            else {
                resultText.append(Text.empty().append(siblings.getFirst()).append(siblings.get(1))
                        .append(Profession.getProfession(siblings.get(2).getString().charAt(0)).getTextWithIcon().setStyle(GRAY)));
            }
        }
    }
}
