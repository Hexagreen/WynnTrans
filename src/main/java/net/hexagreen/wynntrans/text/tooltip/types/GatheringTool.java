package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.ITradableItem;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class GatheringTool extends WynnTooltipText implements ITradableItem {
    private final Profession profession;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return texts.getFirst().getString().matches(". Gathering (Rod|Pickaxe|Axe|Scythe) T\\d");
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString();
        if(!_iName.matches(". Gathering (Rod|Pickaxe|Axe|Scythe) T\\d")) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        GatheringTool tool = new GatheringTool(textBowl);
        tool.translateNameSection(tool.inputText.getSiblings());
        return tool.resultText.getSiblings().getFirst().copy();
    }

    public GatheringTool(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
        this.profession = Profession.getProfession(texts.getFirst().getString().charAt(0));
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
            String _string = seg.getFirst().getString();
            if(i == 0 || _string.matches("Tier \\d+")) {
                translateNameSection(seg);
            }
            else if(_string.contains("Gathering Speed: ")) {
                translateToolSpeedSection(seg);
            }
            else if(_string.matches("^[✔✖].+")) {
                translateReqSection(seg);
            }
            else if(_string.matches("^Gathering Tool .+ Durability]$")) {
                translateTailSection(seg);
            }
            else {
                seg.forEach(resultText::append);
            }
            if(i != segments.size() - 1) resultText.append(SPLITTER);
        });
    }

    private void translateNameSection(List<Text> texts) {
        texts.forEach(t -> {
            String _string = t.getString();
            if(_string.contains(" Gathering ")) {
                resultText.append(profession.getTool(_string.replaceAll("\\D", ""))
                        .setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
            }
            else if(_string.matches("Tier \\d+")) {
                resultText.append(Text.translatable("wytr.unit.tier", _string.replaceAll("\\D", "")).setStyle(GRAY));
            }
            else {
                resultText.append(t);
            }
        });
    }

    private void translateToolSpeedSection(List<Text> texts) {
        List<Text> siblings = texts.getFirst().getSiblings();
        String speedValue = siblings.getFirst().getString().replaceFirst("Gathering Speed: ", "");
        String _speedTier = siblings.getLast().getString().replaceAll("[^A-z]", "");
        Text speedTier = Text.empty().setStyle(GRAY).append(" (")
                .append(Text.translatable("wytr.speed." + normalizeStringForKey(_speedTier))).append(")");
        resultText.append(Text.translatable("wytr.tooltip.gatheringTool.speed", speedValue).setStyle(Style.EMPTY.withColor(Formatting.GOLD))
                .append(speedTier));
    }

    private void translateReqSection(List<Text> texts) {
        texts.forEach(t -> {
            if(t.getString().contains("Lv. Min: ")) {
                Text check = t.getSiblings().getFirst();
                Text profession = this.profession.getIcon().setStyle(Style.EMPTY.withColor(Formatting.WHITE))
                        .append(" ")
                        .append(this.profession.getText().setStyle(GRAY));
                String level = t.getString().replaceAll("\\D", "");
                Text line = Text.empty().setStyle(GRAY).append(check).append(" ")
                        .append(Text.translatable("wytr.requirement.profession", profession, level));
                resultText.append(line);
            }
            else {
                resultText.append(t);
            }
        });
    }

    private void translateTailSection(List<Text> texts) {
        List<Text> siblings = texts.getFirst().getSiblings();
        String[] durability = siblings.getLast().getString().replaceAll("\\[| Durability]", "").split("/");
        Text line = Text.translatable("wytr.tooltip.gatheringTool").setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA))
                .append(" ").append(Text.translatable("wytr.tooltip.equipment.durability", durability[0], durability[1])
                        .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        resultText.append(line);
    }
}
