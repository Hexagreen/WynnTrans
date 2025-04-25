package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AbilityTreeNode extends WynnTooltipText implements ITooltipSplitter, ISpaceProvider {
    private static final Text CLICK_RIGHT = Text.translatable("wytr.tooltip.abilityNode.clickCombo.right")
            .setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true));
    private static final Text CLICK_LEFT = Text.translatable("wytr.tooltip.abilityNode.clickCombo.left")
            .setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withBold(true));

    private final List<Text> tempText;
    private final Deque<Integer> wrapTargetIdx;
    private int longestWidth;
    private String abilityNameKey;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        if(!Pattern.compile("\uE000").matcher(WynnTrans.currentScreen).find()) return false;
        String abilityName = texts.getFirst().getString().replaceAll("§.", "").replaceAll("^Unlock | ability$", "");
        return WTS.checkTranslationDoNotRegister("wytr.ability.node." + normalizeStringForKey(abilityName));
    }

    public AbilityTreeNode(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts, "§[78] ?"));
        this.tempText = new ArrayList<>();
        this.wrapTargetIdx = new ArrayDeque<>();
        this.longestWidth = 150;
        this.abilityNameKey = "wytr.ability.node.UNKNOWN_ABILITY";
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "tooltip.abilityNode";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);

        for(int i = 0, size = segments.size(); i < size; i++) {
            List<Text> seg = segments.get(i);
            Text firstText = seg.getFirst().getSiblings().getFirst();
            String firstTextString = firstText.getString();
            Identifier firstTextFont = firstText.getStyle().getFont();
            if(i == 0) {
                translateNameSection(seg);
            }
            else if(firstTextFont.equals(Identifier.of("minecraft:common"))) {
                translateAttributeSection(seg);
            }
            else if(firstTextString.equals("Unlocking will block:")) {
                translateBlockingInfoSection(seg);
            }
            else if(firstTextString.matches(".+ Archetype$")) {
                translateArchetypeSection(seg);
            }
            else if(firstTextString.matches("[✔✖] ")) {
                translateRequirementSection(seg);
            }
            else if(firstTextString.equals("Blocked by:")) {
                translateBlockedInfoSection(seg);
            }
            else if(firstTextFont.equals(Identifier.of("minecraft:keybind"))
                    || firstTextString.equals("Blocked by another ability")
                    || firstTextString.equals("You do not meet the requirements")) {
                translateAdditionalInfoSection(seg);
            }
            else {
                boolean blockedSkill = segments.getLast().getFirst().getString().matches("^Blocked by.+");
                longestWidth = Math.max(longestWidth, getLongestWidth(seg) + 10);
                translateDescSection(seg, blockedSkill);
            }
            if(i != size - 1) tempText.add(SPLITTER);
        }
        wrapTargetIdx.add(-1);

        for(int i = 0, size = tempText.size(); i < size; i++) {
            if(i == wrapTargetIdx.getFirst()) {
                wrapLine(tempText.get(i), longestWidth).forEach(resultText::append);
                wrapTargetIdx.removeFirst();
            }
            else {
                resultText.append(tempText.get(i));
            }
        }
    }

    private void translateNameSection(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        segment.forEach(t -> {
            List<Text> siblings = t.getSiblings();
            if(siblings.getFirst().getString().equals("Click Combo: ")) {
                List<Text> clicks = new ArrayList<>();
                siblings.forEach(t1 -> {
                    for(String str : t1.getString().split("-")) {
                        if(str.contains("RIGHT")) clicks.add(CLICK_RIGHT);
                        else if(str.contains("LEFT")) clicks.add(CLICK_LEFT);
                    }
                });
                dump.add(Text.translatable(translationKey + ".clickCombo", clicks.toArray(Object[]::new)).setStyle(GRAY));
            }
            else if(t.getString().matches("^Unlock .+ ability$")) {
                dump.add(Text.translatable(translationKey + ".unlock", processAbilityName(siblings.get(1)))
                        .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
            }
            else {
                dump.add(processAbilityName(siblings.getFirst()));
            }
        });

        dump.forEach(this::addTextAndUpdateWidth);
    }

    private Text processAbilityName(Text abilityName) {
        String abilityNameStr = abilityName.getString();
        this.abilityNameKey = "wytr.ability.node." + normalizeStringForKey(abilityNameStr);
        return Text.translatableWithFallback(abilityNameKey, abilityNameStr)
                .setStyle(abilityName.getStyle());
    }

    private void translateDescSection(List<Text> segment, boolean blocked) {
        Style desiredStyle = blocked ? Style.EMPTY.withColor(Formatting.DARK_GRAY) : GRAY;
        StringBuilder collector = new StringBuilder();
        List<MutableText> _args = new ArrayList<>();
        List<Character> _flags = new ArrayList<>();
        segment.forEach(t -> {
            t.getSiblings().forEach(sib -> {
                Style siblingStyle = sib.getStyle();
                TextColor siblingColor = siblingStyle.getColor();
                String siblingString = sib.getString();
                if(siblingStyle.isUnderlined()) {
                    _args.add(sib.copy());
                    _flags.add('A');
                    collector.append("%s");
                }
                else if(!siblingStyle.getFont().equals(Identifier.of("minecraft:default")) || siblingString.matches(" ?[^\u0001-\u007F] ?")) {
                    if(_args.isEmpty()) collector.append("%s");
                    _args.add(Text.literal(siblingString.replaceAll("^ | $", "")).setStyle(siblingStyle));
                    _flags.add('I');
                }
                else if(siblingString.matches("[±+-]?\\d+(\\.\\d+)?(%|s|x|/[35]s| Raw|\\+| Blocks?)? ?")) {
                    _args.add(sib.copy());
                    _flags.add('U');
                    if(siblingString.matches(".+ $"))
                        collector.append("%s ");
                    else collector.append("%s");
                }
                else if(siblingString.matches("[±+-]?\\d+(\\.\\d+)?(%|s|x|/[35]s| Raw|\\+| Blocks?)? [A-z ]+ ?")) {
                    String[] split = siblingString.split(" ", 2);
                    _args.add(Text.literal(split[0]).setStyle(siblingStyle));
                    _args.add(Text.literal(split[1]).setStyle(siblingStyle));
                    _flags.add('U');
                    _flags.add('O');
                    if(siblingString.matches(".+ $"))
                        collector.append("%s %s ");
                    else collector.append("%s %s");
                }
                else if((!blocked && Objects.equals(siblingColor, TextColor.fromFormatting(Formatting.AQUA))
                        || (blocked && Objects.equals(siblingColor, GRAY.getColor())))) {
                    _args.add(sib.copy());
                    _flags.add('O');
                    if(siblingString.matches(".+ $"))
                        collector.append("%s ");
                    else collector.append("%s");
                }
                else if(!blocked && Objects.equals(siblingColor, TextColor.fromFormatting(Formatting.DARK_GRAY))) {
                    collector.append("§8").append(siblingString.replaceAll("%", "%%"));
                }
                else {
                    collector.append(siblingString.replaceAll("%", "%%"));
                }
            });
            collector.append("\n");
        });

        String v = collector.toString().replaceAll("  ", " ").replaceFirst("\\n$", "");
        String k = abilityNameKey + "." + DigestUtils.sha1Hex(v.replaceAll("\\n", " ").replaceAll("  ", " ").replaceAll("§.", "")).substring(0, 4);
        List<MutableText> args = processDescArgs(_args, _flags);
        WTS.checkTranslationExist(k, v);
        tempText.add(Text.translatableWithFallback(k, v.replaceAll("\\n", " "), args.toArray(Object[]::new)).setStyle(desiredStyle));
        wrapTargetIdx.add(tempText.size() - 1);
    }

    private List<MutableText> processDescArgs(List<MutableText> _args, List<Character> _flags) {
        List<MutableText> args = new ArrayList<>();
        for(int i = 0, size = _args.size(); i < size; i++) {
            MutableText current = _args.get(i);
            Style currentStyle = current.getStyle();
            switch(_flags.get(i)) {
                case 'A' -> {
                    String abilityName = current.getString();
                    String k = "wytr.ability.node." + normalizeStringForKey(abilityName);
                    if(WTS.checkTranslationDoNotRegister(k)) {
                        args.add(Text.translatable(k).setStyle(currentStyle));
                    }
                    else {
                        String k1 = "wytr.ability.effectDesc." + normalizeStringForKey(abilityName);
                        WTS.checkTranslationExist(k1, abilityName);
                        args.add(Text.translatableWithFallback(k1, abilityName).setStyle(currentStyle));
                    }
                }
                case 'O' -> {
                    String objectName = current.getString().replaceFirst(" $", "");
                    String k = "wytr.ability.object." + normalizeStringForKey(objectName);
                    WTS.checkTranslationExist(k, objectName);
                    args.add(Text.translatableWithFallback(k, objectName).setStyle(currentStyle));
                }
                case 'I' -> {
                    if(args.isEmpty()) args.add(current);
                    else args.getLast().append(" ").append(current);
                }
                case 'U' -> {
                    String value = current.getString().replaceFirst(" $", "");
                    if(value.contains("Raw")) {
                        String num = value.replaceFirst(" Raw", "");
                        args.add(Text.translatable("wytr.unit.rawID", num).setStyle(currentStyle));
                    }
                    else if(value.contains("Block")) {
                        String num = value.replaceFirst(" Blocks?", "");
                        args.add(Text.translatable("wytr.unit.block", num).setStyle(currentStyle));
                    }
                    else if(value.matches(".+/[35]s")) {
                        String[] split = value.split("/");
                        args.add(Text.literal(split[0]).setStyle(currentStyle).append("/").append(ITime.translateTime(split[1])));
                    }
                    else if(value.contains("s")) {
                        Matcher matcher = Pattern.compile("([±+-]?)(\\d+(\\.\\d+)?s)").matcher(value);
                        if(matcher.find()) {
                            String sign = matcher.group(1);
                            args.add(Text.literal(sign != null ? sign : "").setStyle(currentStyle).append(ITime.translateTime(matcher.group(2))));
                        }
                    }
                    else if(value.contains("x")) {
                        String num = value.replaceFirst("x", "");
                        args.add(Text.translatable("wytr.unit.multiply", num).setStyle(currentStyle));
                    }
                    else {
                        args.add(Text.literal(value).setStyle(currentStyle));
                    }
                }
            }
        }
        return args;
    }

    private void translateAttributeSection(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        segment.forEach(t -> {
            Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
            if(siblings.getFirst().getString().matches("  ÀÀÀÀ\\(?")) {
                dump.add(processDamageInfo(siblings));
            }
            else if(siblings.getFirst().getString().matches("[ À]{20,}.+")) {
                dump.add(processDanglingAttributeInfo(siblings, dump.getLast()));
            }
            else {
                dump.add(processAttributeInfo(siblings));
            }
        });

        dump.forEach(this::addTextAndUpdateWidth);
    }

    private Text processDamageInfo(Deque<Text> siblings) {
        Text last = siblings.removeLast();
        MutableText result = Text.empty();
        siblings.forEach(result::append);
        String[] values = last.getString().split(": ");
        Style lastStyle = last.getStyle();
        if(values[0].equals("Damage")) {
            result.append(Text.translatable(translationKey + ".damageCoef").setStyle(lastStyle));
        }
        else {
            result.append(Text.translatable("wytr.element." + values[0].toLowerCase(Locale.ENGLISH)).setStyle(lastStyle));
        }
        result.append(Text.literal(": ").setStyle(lastStyle));
        result.append(Text.literal(values[1]).setStyle(lastStyle));

        return result;
    }

    private Text processAttributeInfo(Deque<Text> siblings) {
        MutableText result = Text.empty();
        result.append(siblings.removeFirst());
        result.append(siblings.removeFirst());

        Text attributeNameText = siblings.removeFirst();
        String attributeNameString = attributeNameText.getString().replaceAll(": ", "");
        Style attributeNameStyle = attributeNameText.getStyle();
        if(attributeNameString.matches("(Earth|Thunder|Water|Fire|Air) Damage")) {
            String elemName = attributeNameString.split(" ")[0].toLowerCase(Locale.ENGLISH);
            Text elem = Text.translatable("wytr.element." + elemName).setStyle(attributeNameStyle);
            result.append(Text.translatable("wytr.tooltip.baseStat.damage", elem).setStyle(attributeNameStyle));
        }
        else {
            String k = "wytr.ability.attribute." + normalizeStringForKey(attributeNameString);
            WTS.checkTranslationExist(k, attributeNameString);
            result.append(Text.translatableWithFallback(k, attributeNameString).setStyle(attributeNameStyle));
        }
        result.append(Text.literal(": ").setStyle(attributeNameStyle));

        appendAttributeDesc(siblings, result);

        return result;
    }

    private Text processDanglingAttributeInfo(Deque<Text> siblings, Text currentLastLine) {
        MutableText result = Text.empty();

        List<Text> previousSiblings = currentLastLine.getSiblings();
        MutableText previousHeader = Text.empty();
        if(previousSiblings.getFirst().getStyle().getFont().equals(Identifier.of("minecraft:common"))) {
            previousHeader.append(previousSiblings.getFirst())
                    .append(previousSiblings.get(1))
                    .append(previousSiblings.get(2))
                    .append(previousSiblings.get(3));
        }
        else {
            previousHeader.append(previousSiblings.getFirst());
        }

        result.append(getBlanks(WynnTransText.TEXT_RENDERER.getWidth(previousHeader)).toString());
        Text first = siblings.getFirst();
        if(!first.getString().matches("[ À]+")) {
            String shouldNextSibling = first.getString().replaceAll("^[ À]+", "");
            siblings.removeFirst();
            siblings.addFirst(Text.literal(shouldNextSibling).setStyle(first.getStyle()));
        }
        else {
            siblings.removeFirst();
        }

        appendAttributeDesc(siblings, result);

        return result;
    }

    private void appendAttributeDesc(Deque<Text> siblings, MutableText result) {
        Matcher matcher = Pattern.compile("[±~+-]?(\\d+([.-]\\d+)?(s|m|%| Blocks?)?) ?").matcher(siblings.getFirst().getString());
        if(matcher.find()) {
            appendAttributeValue(siblings, matcher, result);
        }
        while(!siblings.isEmpty() && !siblings.getFirst().getString().matches("^ ?\\(.+"))
            appendAttributeBody(siblings, result);
        if(!siblings.isEmpty()) appendAttributeAnnotation(siblings, result);
    }

    private void appendAttributeValue(Deque<Text> siblings, Matcher matcher, MutableText result) {
        Text valueText = siblings.removeFirst();
        String num = matcher.group();
        String str = matcher.replaceFirst("%s");
        boolean trailingSpace = num.matches(".+ $") || (!siblings.isEmpty() && siblings.getFirst().getString().matches("^ .+"));
        if(trailingSpace) num = num.replaceFirst(" $", "");
        if(num.contains("Block"))
            num = Text.translatable("wytr.unit.block", num.replaceFirst(" Blocks?$", "")).getString();
        else if(num.contains("s") || num.contains("m")) {
            String sign = num.replaceFirst("\\d+(\\.\\d)?+[sm]", "");
            num = sign + ITime.translateTime(num.replace(sign, "")).getString();
        }
        if(trailingSpace) num = num + " ";

        Text parsed = Text.literal(num).setStyle(valueText.getStyle());
        if(!str.equals("%s")) {
            String k = "wytr.ability.effect." + DigestUtils.sha1Hex(str).substring(0, 4);
            WTS.checkTranslationExist(k, str);
            result.append(Text.translatableWithFallback(k, valueText.getString(), parsed).setStyle(valueText.getStyle()));
        }
        else result.append(parsed);
    }

    private void appendAttributeBody(Deque<Text> siblings, MutableText result) {
        List<Text> icons = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        Style mergeTargetStyle = siblings.getFirst().getStyle();
        while(true) {
            Text tx = siblings.getFirst();
            String t = tx.getString();
            Style s = tx.getStyle();
            if(s.equals(mergeTargetStyle) || t.matches("\\(")) sb.append(t);
            else if(s.getFont().equals(Identifier.of("minecraft:common")) || t.matches("[^A-z()]")) {
                sb.append("%s");
                icons.add(tx);
            }
            else if(t.matches("\\) .+")) {
                String shouldNextSibling = " " + t.split("\\) ", 2)[1];
                sb.append(t.replaceAll("[^()]", ""));
                siblings.removeFirst();
                siblings.addFirst(Text.literal(shouldNextSibling).setStyle(s));
                continue;
            }
            else break;
            siblings.removeFirst();
            if(siblings.isEmpty()) break;
        }
        String v = sb.toString();

        String[] annotations = {};
        boolean annotationTrailingSpace = false;
        if(v.contains(" to ")) {
            String[] split = v.split(" to ", 2);
            v = split[0] + " ";
            annotationTrailingSpace = split[1].matches(".+ $");
            annotations = ("to " + split[1].replaceFirst(" $", "")).split(", ");
        }

        boolean trailingSpace = v.matches(".+ $");
        if(trailingSpace) v = v.replaceFirst(" $", "");
        String k = "wytr.ability.effect." + DigestUtils.sha1Hex(v).substring(0, 4);
        WTS.checkTranslationExist(k, v);
        MutableText translated = Text.translatableWithFallback(k, v, icons.toArray(Object[]::new)).setStyle(mergeTargetStyle);
        if(trailingSpace) translated.append(" ");

        List<Text> translatedAnnotations = new ArrayList<>();
        for(String annotation : annotations) {
            if(!translatedAnnotations.isEmpty()) translatedAnnotations.add(Text.literal(", "));
            String ka = "wytr.ability.effectDesc." + DigestUtils.sha1Hex(annotation).substring(0, 6);
            WTS.checkTranslationExist(ka, annotation);
            translatedAnnotations.add(Text.translatableWithFallback(ka, annotation));
        }
        if(annotationTrailingSpace) translatedAnnotations.add(Text.literal(" "));
        translatedAnnotations.forEach(translated::append);

        result.append(translated);
    }

    private void appendAttributeAnnotation(Deque<Text> siblings, MutableText result) {
        MutableText translated = Text.empty().setStyle(siblings.getFirst().getStyle()).append("(");
        Deque<Text> args = new ArrayDeque<>();
        StringBuilder sb = new StringBuilder();
        siblings.forEach(t -> {
            if(t.getStyle().isUnderlined()) {
                String abilityName = t.getString();
                String k = "wytr.ability.node." + normalizeStringForKey(abilityName);
                if(WTS.checkTranslationDoNotRegister(k)) {
                    args.add(Text.translatable(k).setStyle(t.getStyle()));
                }
                else {
                    String k1 = "wytr.ability.effectDesc." + normalizeStringForKey(abilityName);
                    WTS.checkTranslationExist(k1, abilityName);
                    args.add(Text.translatableWithFallback(k1, abilityName).setStyle(t.getStyle()));
                }
                sb.append("%s");
            }
            else {
                sb.append(t.getString());
            }
        });
        String[] annotations = sb.toString().replaceFirst("^ ", "").replaceAll("[()]", "").split(", ");
        List<Text> translatedAnnotations = new ArrayList<>();
        for(String annotation : annotations) {
            if(!translatedAnnotations.isEmpty()) translatedAnnotations.add(Text.literal(", "));
            String k = "wytr.ability.effectDesc." + DigestUtils.sha1Hex(annotation).substring(0, 6);
            WTS.checkTranslationExist(k, annotation);
            if(annotation.contains("%s")) {
                Text replacement = args.removeFirst();
                translatedAnnotations.add(Text.translatableWithFallback(k, annotation, replacement));
            }
            else {
                translatedAnnotations.add(Text.translatableWithFallback(k, annotation));
            }
        }
        translatedAnnotations.forEach(translated::append);
        translated.append(")");
        result.append(translated);
    }

    private void translateBlockingInfoSection(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        Style RED = Style.EMPTY.withColor(Formatting.RED);
        dump.add(Text.translatable(translationKey + ".exclusive").setStyle(RED));
        for(int i = 1, size = segment.size(); i < size; i++) {
            Text abilityName = segment.get(i).getSiblings().getLast();
            String v = abilityName.getString();
            String k = "wytr.ability.node." + normalizeStringForKey(v);
            Text translated = Text.literal("- ").setStyle(RED)
                    .append(Text.translatableWithFallback(k, v).setStyle(abilityName.getStyle()));
            dump.add(translated);
        }

        dump.forEach(this::addTextAndUpdateWidth);
    }

    private void translateRequirementSection(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        segment.forEach(t -> {
            MutableText translated = Text.empty();
            Deque<Text> siblings = new ArrayDeque<>(t.getSiblings());
            translated.append(siblings.removeFirst());

            Text _requirement = siblings.removeFirst();
            String str = _requirement.getString();
            if(str.equals("Required Ability: ")) {
                translated.append(Text.translatable(translationKey + ".requirement.ability").setStyle(GRAY));
                Text _ability = siblings.removeFirst();
                Text ability = Text.translatable("wytr.ability.node." + normalizeStringForKey(_ability.getString()))
                        .setStyle(_ability.getStyle());
                translated.append(ability);
            }
            else if(str.equals("Ability Points: ")) {
                translated.append(Text.translatable(translationKey + ".requirement.point").setStyle(GRAY));
            }
            else if(str.matches("Min .+ Archetype: ")) {
                String _archetype = str.replaceAll("^Min | Archetype: $", "");
                Text archetype = Text.translatable("wytr.ability.archetype." + normalizeStringForKey(_archetype));
                translated.append(Text.translatable(translationKey + ".requirement.archetype", archetype).setStyle(GRAY));
            }
            else {
                translated.append(_requirement);
            }
            siblings.forEach(translated::append);
            dump.add(translated);
        });

        dump.forEach(this::addTextAndUpdateWidth);
    }

    private void translateArchetypeSection(List<Text> segment) {
        Text _archetype = segment.getFirst().getSiblings().getFirst();
        Style style = _archetype.getStyle();
        String v = _archetype.getString().replaceFirst(" Archetype$", "");
        String k = "wytr.ability.archetype." + normalizeStringForKey(v);
        Text archetype = Text.translatableWithFallback(k, v).setStyle(style);
        addTextAndUpdateWidth(Text.translatable(translationKey + ".archetype", archetype).setStyle(style));
    }

    private void translateBlockedInfoSection(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        Style RED = Style.EMPTY.withColor(Formatting.RED);
        dump.add(Text.translatable(translationKey + ".blockers").setStyle(RED.withBold(true)));
        for(int i = 1, size = segment.size(); i < size; i++) {
            Text abilityName = segment.get(i).getSiblings().getLast();
            String v = abilityName.getString().replaceFirst("^- ", "");
            String k = "wytr.ability.node." + normalizeStringForKey(v);
            Text translated = Text.literal("- ").setStyle(RED)
                    .append(Text.translatableWithFallback(k, v).setStyle(RED));
            dump.add(translated);
        }

        dump.forEach(this::addTextAndUpdateWidth);
    }

    private void translateAdditionalInfoSection(List<Text> segment) {
        List<Text> dump = new ArrayList<>();
        List<Text> siblings = segment.getFirst().getSiblings();
        if(siblings.getFirst().getStyle().getFont().equals(Identifier.of("minecraft:keybind"))) {
            String k;
            if(siblings.getLast().getString().contains("Click to unlock")) k = ".clickToUnlock";
            else k = ".undoUnlock";
            Text translated = Text.empty().append(siblings.getFirst()).append(" ")
                    .append(Text.translatable(translationKey + k).setStyle(siblings.getLast().getStyle()));
            dump.add(translated);
        }
        else {
            String str = siblings.getFirst().getString();
            if(str.equals("Blocked by another ability")) {
                dump.add(Text.translatable(translationKey + ".blockedBy").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            else if(str.equals("You do not meet the requirements")) {
                dump.add(Text.translatable(translationKey + ".needRequirement").setStyle(Style.EMPTY.withColor(Formatting.RED)));
            }
            else {
                dump.add(segment.getFirst());
            }
        }
        dump.forEach(this::addTextAndUpdateWidth);
    }

    private void addTextAndUpdateWidth(Text text) {
        int width = WynnTransText.TEXT_RENDERER.getWidth(text);
        longestWidth = Math.max(longestWidth, width);
        tempText.add(text);
    }
}
