package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.Equipments;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class UnidentifiedEquipment extends WynnTooltipText implements ITooltipSplitter {
    private static final Style GRAY = Style.EMPTY.withColor(Formatting.GRAY);
    private static final Style GREEN = Style.EMPTY.withColor(Formatting.GREEN);
    public final TextRenderer textRenderer;
    private final List<Text> tempText;
    private int wrappingWidth;
    private Text equip;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches("Unidentified (Helmet|Chestplate|Leggings|Boots|Ring|Necklace|Bracelet|Spear|Dagger|Bow|Wand|Relik)");
    }

    public UnidentifiedEquipment(List<Text> texts) {
        super(texts);
        this.tempText = new ArrayList<>();
        this.wrappingWidth = 120;
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);

        for(int i = 0, size = segments.size(); i < size; i++) {
            if(i == 0) {
                translateNameSection(segments.get(i));
            }
            else if(segments.get(i).getFirst().getString().matches("Info:")) {
                translateInfoSection(segments.get(i));
                break;
            }
            else {
                appendUnknown(segments.get(i));
            }
            tempText.add(SPLITTER);
        }

        tempText.forEach(t -> wrapLine(t, wrappingWidth).forEach(resultText::append));
    }

    private void translateNameSection(List<Text> texts) {
        equip = Equipments.getText(texts.getFirst().getString().replaceFirst("Unidentified ", ""));
        Text head = Text.translatable("wytr.tooltip.equipment.unidentified", equip).setStyle(texts.getFirst().getSiblings().getFirst().getStyle());
        tempText.add(head);
        wrappingWidth = Math.max(wrappingWidth, textRenderer.getWidth(head));
        tempText.add(Text.translatable("wytr.tooltip.equipment.identifyGuide").setStyle(GRAY));
    }

    private void translateInfoSection(List<Text> texts) {
        texts.removeFirst();
        tempText.add(Text.translatable("wytr.func.info").setStyle(GREEN));
        Text bar = Text.literal("- ").setStyle(GREEN);
        for(Text t : texts) {
            List<Text> siblings = t.getSiblings();
            String labelStr = siblings.get(1).getString();
            Text label;
            switch(labelStr) {
                case "Lv. Range: " -> {
                    Text value = siblings.get(2);
                    label = Text.translatable("wytr.tooltip.itemInfo.levelRange", value).setStyle(GRAY);
                }
                case "Tier: " -> {
                    Text value = siblings.get(2);
                    Text rarity = ItemRarity.getRarity(value.getString()).setStyle(value.getStyle());
                    label = Text.translatable("wytr.tooltip.itemInfo.rarity", rarity).setStyle(GRAY);
                }
                case "Type: " -> {
                    Text equipType = equip.copy().setStyle(Style.EMPTY.withColor(Formatting.WHITE));
                    label = Text.translatable("wytr.tooltip.itemInfo.type", equipType).setStyle(GRAY);
                }
                case "- " -> {
                    MutableText guessLine = Text.empty();
                    siblings.subList(0, 7).forEach(guessLine::append);
                    for(Text itemName : siblings.subList(7, siblings.size())) {
                        String nameString = itemName.getString();
                        if(nameString.equals(", ")) guessLine.append(itemName);
                        else guessLine.append(new ItemName(itemName).textAsMutable().setStyle(itemName.getStyle()));
                        if(guessLine.getSiblings().size() <= 9)
                            wrappingWidth = Math.max(wrappingWidth, textRenderer.getWidth(guessLine) + 5);
                    }
                    tempText.add(guessLine);
                    continue;
                }
                default -> {
                    tempText.add(t);
                    continue;
                }
            }
            Text line = Text.empty().append(bar).append(label);
            tempText.add(line);
        }
    }

    private void appendUnknown(List<Text> texts) {
        tempText.addAll(texts);
    }
}
