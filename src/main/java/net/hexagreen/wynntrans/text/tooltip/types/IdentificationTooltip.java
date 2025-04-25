package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.Identifications;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.*;

public class IdentificationTooltip extends WynnTooltipText {

    public IdentificationTooltip(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Deque<Text> lines = new ArrayDeque<>(getSiblings());
        while(!lines.isEmpty()) {
            Text line = lines.removeFirst();
            if(line.getString().isBlank()) {
                resultText.append(" ");
                continue;
            }

            List<Text> siblings = line.getSiblings();
            String str = siblings.getFirst().getString();
            if(siblings.size() > 2 && siblings.get(1).contains(Text.literal(" to "))) {
                resultText.append(translateRangedID(line));
                continue;
            }
            else if(siblings.size() > 3 && siblings.get(2).contains(Text.literal("to"))) {
                resultText.append(translateIngredientRangedID(line));
                continue;
            }
            else if(str.matches("[+-]\\d+(%|/[35]s| tier)?")) {
                resultText.append(translateStaticID(line));
                continue;
            }
            else if(str.matches("\\+.+: ")) {
                lines.addFirst(line);
                resultText.append(translateMajorID(new ArrayList<>(lines)));
                break;
            }

            resultText.append(line);
        }
    }

    private Text translateStaticID(Text text) {
        MutableText result = Text.empty();
        Deque<Text> siblings = new ArrayDeque<>(text.getSiblings());

        Text _idValue = siblings.removeFirst();
        result.append(extractIDValue(_idValue));

        MutableText id = Text.literal(" ");
        Text second = siblings.removeFirst();
        if(second.getString().matches("\\*+")) {
            result.append(second);
            Text tmp = siblings.removeFirst();
            id.append(Identifications.findIdentification(tmp.getString()).getTranslatedText().setStyle(tmp.getStyle()));
        }
        else {
            id.append(Identifications.findIdentification(second.getString()).getTranslatedText().setStyle(second.getStyle()));
        }
        result.append(id);

        siblings.forEach(result::append);

        return result;
    }

    private Text translateMajorID(List<Text> texts) {
        List<Text> merged = mergeTextStyleSide(normalizeMajorIDFont(texts)).getSiblings();

        Text majorID = merged.getFirst();
        Text majorDesc = styleToColorCode(merged.subList(1, merged.size()), Style.EMPTY.withColor(Formatting.DARK_AQUA));
        Identifications id = Identifications.findMajorID(majorID.getString(), majorDesc.getString());

        return Text.literal("+").setStyle(majorID.getStyle())
                .append(id.getTranslatedText()).append(": ")
                .append(colorCodedToStyled(id.getMajorDesc().setStyle(majorDesc.getStyle())));
    }

    private Text translateRangedID(Text text) {
        MutableText result = Text.empty();
        Deque<Text> siblings = new ArrayDeque<>(text.getSiblings());

        Text _firstIDValue = siblings.removeFirst();
        Text firstIDValue = extractIDValue(_firstIDValue);

        Text to = siblings.removeFirst();

        Text _secondIDValue = siblings.removeFirst();
        Text secondIDValue = extractIDValue(_secondIDValue);

        result.append(Text.translatable("wytr.tooltip.equipment.rangedID", firstIDValue, secondIDValue).setStyle(to.getStyle()));

        MutableText id = Text.literal(" ");
        Text idType = siblings.removeFirst();
        id.append(Identifications.findIdentification(idType.getString()).getTranslatedText().setStyle(idType.getStyle()));
        result.append(id);

        return result;
    }

    private Text translateIngredientRangedID(Text text) {
        MutableText result = Text.empty();
        Deque<Text> siblings = new ArrayDeque<>(text.getSiblings());

        Text _firstValue = siblings.removeFirst();
        Text firstValue = extractIDValue(_firstValue);

        siblings.removeFirst();

        Text to = siblings.removeFirst();

        siblings.removeFirst();

        Text _secondValue = siblings.removeFirst();
        Text secondValue = extractIDValue(_secondValue);

        result.append(Text.translatable("wytr.tooltip.equipment.rangedID", firstValue, secondValue).setStyle(to.getStyle()));

        MutableText id = Text.literal(" ");
        Text idType = siblings.removeFirst();
        id.append(Identifications.findIdentification(idType.getString()).getTranslatedText().setStyle(idType.getStyle()));
        result.append(id);

        return result;
    }

    private Text extractIDValue(Text _idValue) {
        Text idValue;
        String _str = _idValue.getString();
        if(_str.matches(".+/[35]s")) {
            String[] str = _str.split("/");
            idValue = Text.literal(str[0] + "/" + ITime.translateTime(str[1]).getString()).setStyle(_idValue.getStyle());
        }
        else if(_str.contains("tier")) {
            String num = _str.replaceFirst(" tier", "");
            idValue = Text.translatable("wytr.tooltip.equipment.attackSpeedID", num).setStyle(_idValue.getStyle());
        }
        else {
            idValue = _idValue;
        }

        return idValue;
    }

    private List<Text> normalizeMajorIDFont(List<Text> texts) {
        List<Text> result = new ArrayList<>();
        for(Text text : texts) {
            MutableText dump = Text.empty();
            text.visit((s, t) -> {
                switch(t) {
                    case "\uE000" -> dump.append(Text.literal("❋")
                            .setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
                    case "\uE001" -> dump.append(Text.literal("✤")
                            .setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN)));
                    case "\uE002" -> dump.append(Text.literal("✹")
                            .setStyle(Style.EMPTY.withColor(Formatting.RED)));
                    case "\uE003" -> dump.append(Text.literal("✦")
                            .setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
                    case "\uE004" -> dump.append(Text.literal("❉")
                            .setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
                    case "\uE005" -> dump.append(Text.literal("✣")
                            .setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
                    default -> {
                        if(s.getColor() == null) dump.append(Text.literal(t).setStyle(s));
                        switch(s.getColor().getHexCode()) {
                            case "#FFFFFF" ->
                                    dump.append(Text.literal(t).setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
                            case "#00AA00" ->
                                    dump.append(Text.literal(t).setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN)));
                            case "#FF5555" ->
                                    dump.append(Text.literal(t).setStyle(Style.EMPTY.withColor(Formatting.RED)));
                            case "#FFFF55" ->
                                    dump.append(Text.literal(t).setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
                            case "#55FFFF" ->
                                    dump.append(Text.literal(t).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
                            case "#FFAA00" ->
                                    dump.append(Text.literal(t).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
                            default -> dump.append(Text.literal(t).setStyle(s));
                        }
                    }
                }
                return Optional.empty();
            }, Style.EMPTY);
            result.add(dump);
        }

        return result;
    }
}
