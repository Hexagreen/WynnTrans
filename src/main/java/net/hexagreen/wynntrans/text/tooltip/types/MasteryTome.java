package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.tooltip.AbstractIdentifiableItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MasteryTome extends AbstractIdentifiableItem {
    private final List<Text> tempText;
    private final Deque<Integer> wrapTargetIdx;
    private int longestWidth;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        String tomeName = normalizeStringForKey(texts.getFirst().getString()
                .replaceFirst("^Unidentified ", "")
                .replaceFirst(" \\[.+%]$", ""))
                .replaceFirst("I+$", "");
        return WTS.checkTranslationDoNotRegister("wytr.item.tome." + tomeName);
    }

    public MasteryTome(List<Text> texts) {
        super(texts);
        this.tempText = new ArrayList<>();
        this.wrapTargetIdx = new ArrayDeque<>();
        this.longestWidth = 0;
        this.textAppender = this::addAndRecordWidth;
        this.optionalTextAppender = this::addAndMarkWrapTarget;
    }

    @Override
    protected void buildStep(List<Text> seg, int step, int lastIndex) {
        String firstSibling = seg.getFirst().getSiblings().getFirst().getString();
        if(step == 0) {
            translateNameSection(seg);
        }
        else if(firstSibling.matches("[✔✖] ?")) {
            translateReqSection(seg);
        }
        else if(firstSibling.matches("[+-]\\d+(%|/[35]s| tier)?")) {
            translateIDSection(seg);
        }
        else if(firstSibling.matches(".+ Raid Reward")) {
            translateTailSection(seg);
        }
        else tempText.addAll(seg);
        if(step != lastIndex) tempText.add(SPLITTER);
        else {
            wrapTargetIdx.add(-1);

            for(int j = 0, size = tempText.size(); j < size; j++) {
                if(j == wrapTargetIdx.getFirst()) {
                    wrapLine(tempText.get(j), Math.max(longestWidth, 150)).forEach(resultText::append);
                    wrapTargetIdx.removeFirst();
                }
                else {
                    resultText.append(tempText.get(j));
                }
            }
        }
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    private void translateNameSection(List<Text> seg) {
        Text __name = seg.getFirst().getSiblings().getFirst();
        boolean unidentified = __name.getString().matches("^Unidentified .+");

        Text percent;
        if(seg.getFirst().getSiblings().size() != 1) percent = seg.getFirst().getSiblings().getLast();
        else percent = Text.empty();
        String tier = __name.getString().replaceAll("֎", "").replaceAll(".+?( I+)?(?: \\[.+%])?$", "$1");
        String _name = __name.getString()
                .replaceFirst("^Unidentified ", "")
                .replaceAll("֎", "")
                .replaceAll("( I+)?(?: \\[.+%])?$", "");
        Text name = Text.empty().setStyle(__name.getStyle())
                .append(Text.translatableWithFallback("wytr.item.tome." + normalizeStringForKey(_name), _name))
                .append(tier).append(percent);

        MutableText translated;
        if(unidentified) {
            translated = Text.translatable("wytr.tooltip.equipment.unidentified", name)
                    .setStyle(__name.getStyle());
        }
        else translated = name.copy();
        addAndRecordWidth(translated);
        seg.removeFirst();
        seg.forEach(t -> {
            String string = normalizeStringForKey(t.getString());
            if(string.equals("Thisitemspowershave")) {
                addAndMarkWrapTarget(Text.translatable("wytr.tooltip.equipment.identifyGuide").setStyle(GRAY));
            }
            else if(string.matches("Fromchat")) {
                addAndRecordWidth(Text.translatable("wytr.func.wynntilsCompat.sharedGearStat.fromChat").formatted(Formatting.ITALIC, Formatting.DARK_GRAY));
            }
        });
    }

    protected void translateTailSection(List<Text> texts) {
        for(Text t : texts) {
            String string = t.getString();
            if(string.matches("Untradable Item")) {
                addAndRecordWidth(Text.translatable("wytr.tooltip.item.untradable").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            else if(string.matches(".+ Raid Reward$")) {
                String[] splits = string.split(" ", 2);
                Text rarity = ItemRarity.getRarity(splits[0]);
                addAndRecordWidth(Text.translatable("wytr.tooltip.item.raidReward", rarity).setStyle(rarity.getStyle()));
            }
            else addAndRecordWidth(t);
        }
    }

    private void updateLongestWidth(int width) {
        longestWidth = Math.max(longestWidth, width);
    }

    protected void addAndRecordWidth(Text text) {
        updateLongestWidth(WynnTransText.TEXT_RENDERER.getWidth(text));
        tempText.add(text);
    }

    protected void addAndMarkWrapTarget(Text text) {
        tempText.add(text);
        wrapTargetIdx.add(tempText.size() - 1);
    }
}
