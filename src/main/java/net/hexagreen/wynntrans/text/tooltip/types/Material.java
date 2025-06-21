package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.EquipmentAndConsumables;
import net.hexagreen.wynntrans.text.tooltip.AbstractWynnItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Material extends AbstractWynnItem {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        String materialName = texts.getFirst().getString().replaceAll("À", "")
                .replaceAll("§.", "").replaceFirst(" \\[✫✫✫]", "");
        return WTS.checkTranslationDoNotRegister("wytr.item.material." + normalizeStringForKey(materialName));
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString().replaceFirst(" \\[✫✫✫]", "");
        if(!WTS.checkTranslationDoNotRegister("wytr.item.material." + normalizeStringForKey(_iName))) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        Material material = new Material(textBowl);
        material.translateNameSection(material.inputText.getSiblings());
        return material.resultText.getSiblings().getFirst().copy();
    }

    public Material(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void buildStep(List<Text> seg, int step, final int lastIndex) {
        String _string = seg.getFirst().getString();
        if(step == 0 || _string.equals("Crafting Material")) {
            translateNameSection(seg);
        }
        else if(_string.matches("^[✔✖].+")) {
            translateReqSection(seg);
        }
        else if(_string.equals("Use this material to purchase")) {
            translateUseSection(seg);
        }
        else {
            seg.forEach(resultText::append);
        }
        if(step != lastIndex) resultText.append(SPLITTER);
    }

    private void translateNameSection(List<Text> texts) {
        texts.forEach(t -> {
            String _string = t.getString();
            if(_string.replaceAll("À", "").matches(".+ \\[✫✫✫]$")) {
                MutableText materialName = new ItemName(t.getSiblings().getFirst().getString()).textAsMutable()
                        .setStyle(t.getSiblings().getFirst().getStyle());
                t.getSiblings().subList(1, t.getSiblings().size()).forEach(materialName::append);
                resultText.append(materialName);
            }
            else if(t.getString().equals("Crafting Material")) {
                resultText.append(Text.translatable("wytr.tooltip.material").setStyle(GRAY));
            }
            else {
                resultText.append(t);
            }
        });
    }

    private void translateUseSection(List<Text> texts) {
        List<Text> icons = new ArrayList<>();
        List<Text> products = new ArrayList<>();
        for(int i = 2, size = texts.size(); i < size; i++) {
            Matcher matcher = Pattern.compile("([Ⓐ-Ⓛ]) (\\w+)").matcher(texts.get(i).getString());
            Style white = Style.EMPTY.withColor(Formatting.WHITE);
            while(matcher.find()) {
                icons.add(Text.literal(matcher.group(1)).setStyle(white));
                String _product = switch(matcher.group(2)) {
                    case "Leggings", "Boots" -> matcher.group(2);
                    default -> matcher.group(2).replaceFirst("s$", "");
                };
                products.add(EquipmentAndConsumables.getText(_product).setStyle(GRAY));
            }
        }

        MutableText craftedItems = Text.empty().setStyle(GRAY);
        for(int j = 0, size2 = icons.size(); j < size2; j++) {
            if(j > 0) {
                if(j != size2 - 1) craftedItems.append(", ");
                else craftedItems.append(Text.translatable("wytr.and"));
            }
            craftedItems.append(icons.get(j)).append(" ").append(products.get(j));
        }
        wrapLine(Text.translatable("wytr.tooltip.material.use", craftedItems).setStyle(GRAY), 150).forEach(resultText::append);
    }
}
