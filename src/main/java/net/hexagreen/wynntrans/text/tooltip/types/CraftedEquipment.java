package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.EquipmentAndConsumables;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CraftedEquipment extends NormalEquipment {

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        for(int i = texts.size() - 1; i >= 0; i--) {
            if(texts.get(i).getString().matches("^(§7)?Crafted .+ \\[\\d+/\\d+ Durability]"))
                return true;
        }
        return false;
    }

    public CraftedEquipment(List<Text> texts) {
        super(texts);
        itemTailRegex = Pattern.compile("^Crafted .+ \\[\\d+/\\d+ Durability]$|Powder Slots");
    }

    @Override
    protected void translateTailSection(List<Text> texts) {
        for(int i = 0, size = texts.size(); i < size; i++) {
            Text t = texts.get(i);
            String string = t.getString();
            Text body;
            if(string.contains("Powder Slots")) {
                Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
                String[] powderInfo = siblings.removeFirst().getString().split(" Powder Slots ?");
                MutableText applied = Text.empty();
                if(powderInfo.length == 2) {
                    siblings.addFirst(Text.literal(powderInfo[1]));
                    siblings.forEach(applied::append);
                }
                body = Text.translatable("wytr.tooltip.equipment.powderSlot", powderInfo[0], applied).setStyle(GRAY);
                addAndRecordWidth(body);
                continue;
            }
            Matcher m = Pattern.compile("^Crafted (.+) \\[(\\d+)/(\\d+) Durability]$").matcher(string);
            if(m.matches()) {
                Text rarity = ItemRarity.CRAFTED.getRarity();
                Text durability = Text.translatable("wytr.tooltip.equipment.durability", m.group(2), m.group(3))
                        .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
                String _signature = m.group(1);
                if(_signature.matches("by .+")) {
                    String creator = _signature.replaceFirst("by ", "");
                    body = Text.empty().setStyle(rarity.getStyle())
                            .append(Text.translatable("wytr.tooltip.equipment.creator", creator)).append(" ")
                            .append(durability);
                }
                else {
                    Text type = EquipmentAndConsumables.getText(_signature.replaceFirst("Pants", "Leggings"));
                    body = Text.empty().setStyle(rarity.getStyle())
                            .append(rarity).append(" ")
                            .append(type).append(" ")
                            .append(durability);
                }
                addAndRecordWidth(body);
            }
            else {
                addAndMarkWrapTarget(mergeTextStyleSide(texts.subList(i, size)));
                return;
            }
        }
    }
}
