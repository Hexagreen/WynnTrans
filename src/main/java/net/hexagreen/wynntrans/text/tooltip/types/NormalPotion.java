package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.hexagreen.wynntrans.enums.Identifications;
import net.hexagreen.wynntrans.text.tooltip.ConsumableItem;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NormalPotion extends ConsumableItem {
    private final List<Text> tempText;
    private final Deque<Integer> wrapTargetIdx;
    private final List<String> descArgs;
    private int longestWidth;
    private CharacterSkill potionElement;
    private String key;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        return Pattern.compile("Potion of (Healing|Mana|§.[✤✦❉✹❋])").matcher(texts.getFirst().getString()).find();
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString();
        if(!Pattern.compile("Potion of (Healing|Mana|§.[✤✦❉✹❋])").matcher(_iName).find()) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        NormalPotion potion = new NormalPotion(textBowl);
        potion.translateNameSection(potion.inputText.getSiblings());
        return potion.resultText.getSiblings().getFirst().copy();
    }

    public NormalPotion(List<Text> texts) {
        super(texts);
        this.tempText = new ArrayList<>();
        this.longestWidth = 150;
        this.wrapTargetIdx = new ArrayDeque<>();
        this.textAppender = this::addAndRecordWidth;
        this.optionalTextAppender = this::addAndMarkWrapTarget;
        this.descArgs = new ArrayList<>();
    }

    @Override
    protected void build() {
        super.build();
        wrapTargetIdx.add(-1);

        for(int j = 0, size = tempText.size(); j < size; j++) {
            if(j == wrapTargetIdx.getFirst()) {
                wrapLine(tempText.get(j), longestWidth).forEach(resultText::append);
                wrapTargetIdx.removeFirst();
            }
            else {
                resultText.append(tempText.get(j));
            }
        }
    }

    @Override
    protected void additionalStep(String _string, List<Text> seg) {
        if(_string.equals("Drinking this potion will")) {
            Matcher timeMatcher = Pattern.compile("(\\d+) seconds").matcher(seg.getLast().getString());
            if(timeMatcher.find()) descArgs.add(timeMatcher.group(1));
            else descArgs.add("0");
            addAndMarkWrapTarget(Text.translatable(key + ".desc", descArgs.toArray(Object[]::new))
                    .setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        }
        else seg.forEach(this::addAndRecordWidth);
    }

    @Override
    protected void translateNameSection(List<Text> texts) {
        Deque<Text> nameSiblings = new ArrayDeque<>(texts.getFirst().getSiblings());
        Text _name = nameSiblings.removeFirst();
        Text count;
        if(nameSiblings.getLast().getString().matches(" \\[\\d/\\d]")) count = nameSiblings.removeLast();
        else count = Text.empty();
        Text name;
        switch(_name.getString()) {
            case "Potion of Healing" -> {
                key = "wytr.item.misc.healPotion";
                potionElement = null;
                name = Text.translatable(key).setStyle(_name.getStyle()).append(count);
            }
            case "Potion of Mana" -> {
                key = "wytr.item.misc.manaPotion";
                potionElement = null;
                name = Text.translatable(key).setStyle(_name.getStyle()).append(count);
            }
            case "Potion of " -> {
                key = "wytr.item.misc.statPotion";
                potionElement = CharacterSkill.getSkill(nameSiblings.getFirst().getString().replaceFirst("[✤✦❉✹❋] ", ""));
                name = Text.translatable(key, potionElement.getStatWithIcon()).setStyle(_name.getStyle()).append(count);
            }
            default -> throw new TextTranslationFailException("Unknown potion name");
        }
        addAndRecordWidth(name);
    }

    @Override
    protected void buildEffectBody(MutableText body, Text type, Deque<Text> siblings) {
        if(type.getString().equals("Effect: ")) {
            body.append(Text.translatable("wytr.tooltip.consumable.effect").setStyle(type.getStyle()));
            Text last = siblings.removeLast();
            if(Pattern.compile(" [✤✦❉✹❋] .+").matcher(last.getString()).find()) {
                String num = last.getString().split(" [✤✦❉✹❋]")[0];
                if(!num.isBlank()) siblings.addFirst(Text.literal(num).setStyle(last.getStyle()));
                body.append(siblings.getFirst()).append(" ").append(potionElement.getStatWithIcon());
                descArgs.add(potionElement.getStatWithIcon().getString());
                descArgs.add(siblings.getFirst().getString().replace("+", ""));
            }
            else {
                String[] split = last.getString().split(" ", 2);
                Text num = Text.literal(split[0]).setStyle(last.getStyle());
                Text id = Identifications.findIdentification(split[1]).getTranslatedText().setStyle(last.getStyle());
                body.append(num).append(" ").append(id);
                descArgs.add(id.getString());
                descArgs.add(num.getString());
            }
        }
        else if(type.getString().equals("Heal: ")) {
            body.append(Text.translatable("wytr.tooltip.consumable.heal").setStyle(type.getStyle()));
            siblings.forEach(body::append);
        }
        else if(type.getString().equals("Mana: ")) {
            body.append(Text.translatable("wytr.tooltip.consumable.mana").setStyle(type.getStyle()));
            siblings.forEach(body::append);
        }
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    private void updateLongestWidth(int width) {
        longestWidth = Math.max(longestWidth, width);
    }

    private void addAndRecordWidth(Text text) {
        updateLongestWidth(TEXT_RENDERER.getWidth(text));
        tempText.add(text);
    }

    private void addAndMarkWrapTarget(Text text) {
        tempText.add(text);
        wrapTargetIdx.add(tempText.size() - 1);
    }
}
