package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.EquipmentAndConsumables;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.minecraft.text.Text;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CraftedPotion extends NormalPotion {
    private static final Pattern CRAFTED_TAG = Pattern.compile("^Crafted (Potion|Food|Scroll|by (?!.*\\[)(.+))$");

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        for(int i = texts.size() - 1; i >= 0; i--) {
            if(CRAFTED_TAG.matcher(texts.get(i).getString().replaceAll("§.", "")).find())
                return true;
        }
        return false;
    }

    public CraftedPotion(List<Text> texts) {
        super(texts);
    }

    @Override
    protected void additionalStep(String _string, List<Text> seg) {
        Matcher m = CRAFTED_TAG.matcher(_string);
        if(seg.getFirst().getSiblings().getFirst().getString().matches("[+-]\\d+(%|/[35]s| tier)?")) {
            new IdentificationTooltip(seg).text().forEach(this::addAndRecordWidth);
        }
        else if(m.matches()) {
            Text rarity = ItemRarity.CRAFTED.getRarity();
            String creator = m.group(2);
            Text body;
            if(creator != null) {
                body = Text.empty().setStyle(rarity.getStyle())
                        .append(Text.translatable("wytr.tooltip.equipment.creator", creator));
            }
            else {
                Text type = EquipmentAndConsumables.getText(m.group(1));
                body = Text.empty().setStyle(rarity.getStyle())
                        .append(rarity).append(" ")
                        .append(type);
            }
            addAndRecordWidth(body);

            if(seg.size() > 1) {
                addAndMarkWrapTarget(mergeTextStyleSide(seg.subList(1, seg.size())));
            }
        }
        else seg.forEach(this::addAndRecordWidth);
    }
}
