package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.Identifications;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

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
        List<Text> lines = new ArrayList<>(getSiblings());
        while(!lines.isEmpty()) {
            Text line = lines.removeFirst();
            if(line.getString().isBlank()) {
                resultText.append(" ");
                continue;
            }

            String str = line.getSiblings().getFirst().getString();
            if(str.matches("[+-]\\d+(%|/[35]s| Tiers)?")) {
                resultText.append(translateStaticID(line));
                continue;
            }
            else if(str.matches("\\+.+: ")) {
                lines.addFirst(line);
                resultText.append(translateMajorID(lines));
                break;
            }

            resultText.append(line);
        }
    }

    private Text translateStaticID(Text text) {
        MutableText result = Text.empty();
        List<Text> siblings = new ArrayList<>(text.getSiblings());

        Text _idValue = siblings.removeFirst();
        Text idValue;
        String _str = _idValue.getString();
        if(_str.matches(".+/[35]s")) {
            String[] str = _str.split("/");
            idValue = Text.literal(str[0] + "/" + ITime.translateTime(str[1]).getString()).setStyle(_idValue.getStyle());
        }
        else if(_str.contains("Tiers")) {
            String num = _str.replaceFirst(" Tiers", "");
            idValue = Text.translatable("wytr.tooltip.idAtkSpdTier", num).setStyle(_idValue.getStyle());
        }
        else {
            idValue = _idValue;
        }
        result.append(idValue);
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
        List<Text> merged = mergeTextStyleSide(texts).getSiblings();

        Text majorID = merged.getFirst();
        Text majorDesc = styleToColorCode(merged.subList(1, merged.size()), Style.EMPTY.withColor(Formatting.DARK_AQUA));
        Identifications id = Identifications.findMajorID(majorID.getString(), majorDesc.getString());

        return Text.literal("+").setStyle(majorID.getStyle())
                .append(id.getTranslatedText()).append(": ")
                .append(id.getMajorDesc().setStyle(majorDesc.getStyle()));
    }
}
