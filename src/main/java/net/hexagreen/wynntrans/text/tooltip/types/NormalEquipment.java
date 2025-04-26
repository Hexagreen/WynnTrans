package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.tooltip.IPricedItem;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.hexagreen.wynntrans.text.WynntilsCompatible.coloringDefectiveItem;
import static net.hexagreen.wynntrans.text.WynntilsCompatible.coloringPerfectItem;

public class NormalEquipment extends WynnTooltipText implements ITooltipSplitter, IPricedItem {
    private static final Pattern NAME_ATTACHMENT_REGEX =
            Pattern.compile("Attack Speed|This item's powers have");
    private static final Pattern ITEM_TAIL_REGEX =
            Pattern.compile(".+ Item( \\[\\d+])?$|Powder Slots");
    private static final Pattern BASE_STAT_REGEX =
            Pattern.compile("❤ Health: |[✤✦❉✹❋] .+ Defence: |[✣✤✦❉✹❋] .+ Damage: ");
    private static final Pattern POWDER_SPECIAL_REGEX =
            Pattern.compile(" (Quake|Chain Lightning|Curse|Courage|Wind Prison|Rage|Kill Streak|Concentration|Endurance|Dodge) [IV]+");
    private final List<Text> tempText;
    private final Deque<Integer> wrapTargetIdx;
    private int longestWidth;
    private String itemNameKey = "";

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 2) return false;
        if(Pattern.compile("[\uE000\uE001\uE002\uE004]").matcher(WynnTrans.currentScreen).find()) return false;
        if(texts.get(1).getString().contains("Sold")) return false;
        String itemName = texts.getFirst().getString().replaceAll("^Unidentified |^Perfect |^Defective |§f⬡ Shiny |\\[.+%]$", "");
        String normalized = normalizeStringForKey(itemName);
        return WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalized);
    }

    public static MutableText getTranslatedItemName(Text text) {
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        String itemName = text.getString().replaceAll("^Unidentified |^Perfect |^Defective |§f⬡ Shiny |\\[.+%]$", "");
        String normalized = normalizeStringForKey(itemName);
        if(WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalized)) {
            NormalEquipment equipment = new NormalEquipment(textBowl);
            equipment.translateNameSection(equipment.inputText.getSiblings());
            return equipment.tempText.getFirst().copy();
        }
        else return null;
    }

    public NormalEquipment(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
        this.tempText = new ArrayList<>();
        this.wrapTargetIdx = new ArrayDeque<>();
        this.longestWidth = 150;
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        splitTooltipToSegments(getSiblings(), segments);
        ItemTooltipPriceSection priceSection = detachPriceSection(segments);
        int priceSectionIndex = priceSection.position();

        for(int i = 0, size = segments.size(); i < size; i++) {
            if(i == 0) {
                translateNameSection(segments.getFirst());
                tempText.add(SPLITTER);
                continue;
            }
            else if(i == priceSectionIndex) {
                priceSection.priceTexts().forEach(this::addAndRecordWidth);
                tempText.add(SPLITTER);
            }

            List<Text> seg = segments.get(i);
            if(NAME_ATTACHMENT_REGEX.matcher(seg.getFirst().getString()).find()) {
                translateNameSection(seg);
            }
            else if(BASE_STAT_REGEX.matcher(seg.getFirst().getString()).find()) {
                translateBaseStatSection(seg);
            }
            else if(POWDER_SPECIAL_REGEX.matcher(seg.getFirst().getSiblings().getLast().getString()).find()) {
                translatePowderSpecialSection(seg);
            }
            else if(seg.getFirst().getSiblings().getFirst().getString().matches("[✔✖] ?")) {
                translateReqSection(seg);
            }
            else if(seg.getFirst().getSiblings().getFirst().getString().equals("⬡ ")) {
                translateShinySection(seg);
            }
            else if(seg.getFirst().getSiblings().getFirst().getString().matches("[+-]\\d+(%|/[35]s| Tiers)?")) {
                translateIDSection(seg);
            }
            else if(seg.getFirst().getString().matches(".+ Set \\(\\d/\\d\\)")) {
                translateSetPartsSection(seg);
            }
            else if(seg.getFirst().getString().equals("Set Bonus:")) {
                translateSetBonusSection(seg);
            }
            else if(ITEM_TAIL_REGEX.matcher(seg.getFirst().getString()).find()) {
                translateTailSection(seg);
                break;
            }
            else {
                appendUnknowns(seg);
            }
            tempText.add(SPLITTER);
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

    private void translateNameSection(List<Text> texts) {
        for(Text t : texts) {
            String itemNameString = t.getString().replaceAll("^Unidentified |^Perfect |^Defective |⬡ Shiny | \\[.+%]$", "");
            String normalized = normalizeStringForKey(itemNameString);
            boolean shiny = t.getString().matches(".*⬡ Shiny.+");
            boolean unidentified = t.getString().matches("^Unidentified .+");
            boolean wynntilsPerfect = t.getString().matches("^Perfect .+");
            boolean wynntilsDefective = t.getString().matches("^Defective .+");
            boolean wynntilsPercentage = t.getString().matches(".+ \\[.+%]$");
            MutableText itemName = Text.empty();
            if(shiny) {
                itemName.append(Text.translatable("wytr.tooltip.equipment.shiny"));
            }
            if(normalized.equals("Thisitemspowershave")) {
                tempText.add(Text.translatable("wytr.tooltip.equipment.identifyGuide").setStyle(GRAY));
                wrapTargetIdx.add(tempText.size() - 1);
                break;
            }
            else if(normalized.matches(".+AttackSpeed$")) {
                addAndRecordWidth(Text.translatable("wytr.tooltip.attackSpeed." + normalized.replaceFirst("AttackSpeed", "")).setStyle(GRAY));
                continue;
            }
            else if(WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalized)) {
                itemNameKey = "wytr.item.weapon." + normalized;
                itemName.append(Text.translatable(itemNameKey));
            }
            else if(WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalized)) {
                itemNameKey = "wytr.item.armour." + normalized;
                itemName.append(Text.translatable(itemNameKey));
            }
            else if(WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalized)) {
                itemNameKey = "wytr.item.accessory." + normalized;
                itemName.append(Text.translatable(itemNameKey));
            }
            else {
                if(shiny) itemName.append(t.getSiblings().get(1).getString().replaceFirst("Shiny ", ""));
                else itemName.append(t.getSiblings().getFirst().copy());
            }

            MutableText translated;
            if(unidentified) {
                translated = Text.translatable("wytr.tooltip.equipment.unidentified", itemName)
                        .setStyle(t.getSiblings().getFirst().getStyle());
            }
            else if(wynntilsPerfect) {
                Text perfect = Text.translatable("wytr.tooltip.equipment.perfectID", itemName);
                translated = coloringPerfectItem(perfect);
            }
            else if(wynntilsDefective) {
                Text defective = Text.translatable("wytr.tooltip.equipment.defectiveID", itemName);
                translated = coloringDefectiveItem(defective);
            }
            else {
                if(shiny) translated = itemName.copy().setStyle(t.getSiblings().get(1).getStyle());
                else translated = itemName.copy().setStyle(t.getSiblings().getFirst().getStyle());
            }

            if(wynntilsPercentage) {
                translated.append(t.getSiblings().getLast());
            }

            addAndRecordWidth(translated);
        }
    }

    private void translateBaseStatSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        texts.forEach(t -> {
            Style headStyle = t.getSiblings().getFirst().getStyle();
            String[] split = t.getString().split(": ");
            String[] icon = split[0].split(" ");
            Text line;
            if(split[0].contains("Health")) {
                line = Text.literal("À" + icon[0] + " ").setStyle(headStyle)
                        .append(Text.translatable("wytr.tooltip.baseStat.health"))
                        .append(": " + split[1]);
            }
            else if(split[0].contains("Neutral")) {
                Text neutral = Text.literal(icon[0] + " ")
                        .append(Text.translatable("wytr.element.neutral"));
                line = Text.translatable("wytr.tooltip.baseStat.damage", neutral).setStyle(headStyle)
                        .append(": " + split[1]);
            }
            else if(split[0].contains("DPS")) {
                line = Text.empty().append(t.getSiblings().getFirst()).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))
                        .append(Text.translatable("wytr.tooltip.baseStat.dps"))
                        .append(": " + split[1]);
            }
            else {
                String typeKey;
                if(split[0].contains("Damage")) {
                    typeKey = "wytr.tooltip.baseStat.damage";
                }
                else if(split[0].contains("Defence")) {
                    typeKey = "wytr.tooltip.baseStat.defence";
                }
                else {
                    dump.add(t);
                    return;
                }

                Text element = Text.literal(icon[0] + " ").setStyle(headStyle)
                        .append(Text.translatable("wytr.element." + icon[1].toLowerCase(Locale.ENGLISH)));
                line = Text.translatable(typeKey, element).setStyle(GRAY)
                        .append(": " + split[1]);
            }
            dump.add(line);
        });

        dump.forEach(this::addAndRecordWidth);
    }

    private void translatePowderSpecialSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        Deque<Text> mutableSegment = new ArrayDeque<>(texts);
        Deque<Text> _firstLine = new ArrayDeque<>(mutableSegment.removeFirst().getSiblings());
        Text _skillName = _firstLine.removeLast();
        Matcher matcher = Pattern.compile(" (.+)( [IV]+)(?: \\[(.+)])?").matcher(_skillName.getString());
        MutableText firstLine = Text.empty();
        _firstLine.forEach(firstLine::append);
        if(matcher.find()) {
            String name = normalizeStringForKey(matcher.group(1));
            String tier = matcher.group(2);
            String desc = matcher.group(3);

            String k = "wytr.powderSpecial." + name;
            MutableText skillName = Text.empty().setStyle(_skillName.getStyle())
                    .append(" ").append(Text.translatable(k)).append(tier);
            if(desc != null) {
                skillName.append(" [").append(Text.translatable(k + ".desc")).append("]");
            }

            firstLine.append(skillName);
        }
        else {
            firstLine.append(_skillName);
        }
        dump.add(firstLine);

        for(Text text : mutableSegment) {
            MutableText line = Text.empty();
            Deque<Text> siblings = new ArrayDeque<>(text.getSiblings());
            line.append(siblings.removeFirst());

            String _effect = siblings.removeFirst().getString();
            Matcher numFinder = Pattern.compile("\\+?(?:\\d+\\.\\d+|\\d+)+(s|%| Blocks)?").matcher(_effect);
            if(numFinder.find()) {
                String num = numFinder.group();
                String effectFormat = _effect.replace(num, "%s").replaceFirst(" ?$", " ");
                if(num.matches(".+ Blocks$"))
                    num = Text.translatable("wytr.unit.block", num.replaceFirst(" Blocks", "")).getString();
                else if(num.contains("s")) num = ITime.translateTime(num).getString();
                String effectKey = "wytr.tooltip.powderSpecial." + normalizeStringForKey(_effect.split(":")[0]);
                WTS.checkTranslationExist(effectKey, effectFormat);
                line.append(Text.translatableWithFallback(effectKey, _effect, num).setStyle(GRAY));
            }
            else {
                line.append(Text.literal(_effect).setStyle(GRAY));
            }

            for(Text trail : siblings) {
                if(trail.getString().isBlank() || trail.getStyle().getFont().equals(Identifier.of("minecraft:common"))) {
                    line.append(trail);
                }
                else {
                    String additionalInfo = trail.getString();
                    String k = "wytr.tooltip.powderSpecial.info." + DigestUtils.sha1Hex(additionalInfo).substring(0, 4);
                    WTS.checkTranslationExist(k, additionalInfo);
                    line.append(Text.translatableWithFallback(k, additionalInfo).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
                }
            }
            dump.add(line);
        }

        dump.forEach(this::addAndRecordWidth);
    }

    private void translateReqSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        texts.forEach(t -> {
            String[] headAndNum = t.getSiblings().getLast().getString().split(": ");
            Text icon = t.getSiblings().getFirst();
            Text reformedIcon = Text.literal(icon.getString().replaceFirst(" ", "")).setStyle(icon.getStyle());
            Text body;
            if(headAndNum[0].contains("Class Req")) {
                String className = headAndNum[1].split("/")[0].toLowerCase(Locale.ENGLISH);
                Text reqClass = Text.translatable("wytr.class." + className)
                        .append("/").append(Text.translatable("wytr.class." + className + "_alt"));
                body = Text.translatable("wytr.requirement.class", reqClass);
            }
            else if(headAndNum[0].contains("Combat Lv. Min")) {
                body = Text.translatable("wytr.requirement.combat", headAndNum[1]);
            }
            else if(headAndNum[0].matches(" (Strength|Dexterity|Intelligence|Defence|Agility) Min")) {
                String skillName = headAndNum[0].split(" ")[1].toLowerCase(Locale.ENGLISH);
                Text reqSkill = Text.translatable("wytr.skill." + skillName);
                body = Text.translatable("wytr.requirement.skill", reqSkill, headAndNum[1]);
            }
            else if(headAndNum[0].contains("Quest Req")) {
                String questVal = headAndNum[1];
                String questKey = "wytr.quest." + normalizeStringForKey(questVal);
                WTS.checkTranslationExist(questKey, questVal);
                Text questName = Text.translatableWithFallback(questKey, questVal);
                body = Text.translatable("wytr.requirement.quest", questName);
            }
            else {
                dump.add(t);
                return;
            }

            Text line = Text.empty().setStyle(GRAY)
                    .append(reformedIcon).append(" ").append(body);
            dump.add(line);
        });

        dump.forEach(this::addAndRecordWidth);
    }

    private void translateShinySection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        texts.forEach(t -> {
            Text icon = t.getSiblings().getFirst();
            Text number = t.getSiblings().getLast();
            String v = t.getSiblings().get(1).getString().replaceFirst(": ", "");
            String k = "wytr.shiny." + normalizeStringForKey(v);
            WTS.checkTranslationExist(k, v);
            Text tracker = Text.translatable(k).setStyle(GRAY).append(": ");
            dump.add(Text.empty().append(icon).append(tracker).append(number));
        });
        dump.forEach(this::addAndRecordWidth);
    }

    private void translateIDSection(List<Text> texts) {
        List<Text> translatedIDs = new IdentificationTooltip(texts).text();
        translatedIDs.forEach(t -> {
            tempText.add(t);
            if(t.getContent().toString().equals("empty")) {
                updateLongestWidth(TEXT_RENDERER.getWidth(t));
            }
            else {
                wrapTargetIdx.add(tempText.size() - 1);
            }
        });
    }

    private void translateSetPartsSection(List<Text> texts) {
        List<Text> siblings = texts.getFirst().getSiblings();
        String setNameVal = siblings.getFirst().getString().replaceFirst(" Set $", "");
        String setNameKey = "wytr.setItem." + normalizeStringForKey(setNameVal);
        WTS.checkTranslationExist(setNameKey, setNameVal);
        Text setName = Text.translatableWithFallback(setNameKey, setNameVal);
        Text head = Text.translatable("wytr.tooltip.equipment.setFamily", setName).setStyle(siblings.getFirst().getStyle())
                .append(" ").append(siblings.getLast());
        addAndRecordWidth(head);
        texts.subList(1, texts.size()).forEach(this::addAndRecordWidth);
    }

    private void translateSetBonusSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        dump.add(Text.translatable("wytr.tooltip.equipment.setBonus").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        dump.addAll(new IdentificationTooltip(texts.subList(1, texts.size())).text());
        dump.forEach(this::addAndRecordWidth);
    }

    private void translateTailSection(List<Text> texts) {
        for(int i = 0, size = texts.size(); i < size; i++) {
            Text t = texts.get(i);
            String string = t.getString();
            Text body;
            if(string.contains("Powder Slots")) {
                String[] powderInfo = styleToColorCode(t.getSiblings(), GRAY).getString().split(" Powder Slots ?");
                body = Text.translatable("wytr.tooltip.equipment.powderSlot", powderInfo[0], powderInfo.length == 2 ? powderInfo[1] : "").setStyle(GRAY);
                addAndRecordWidth(body);
            }
            else if(string.matches("Untradable Item")) {
                body = Text.translatable("wytr.tooltip.item.untradable").setStyle(Style.EMPTY.withColor(Formatting.RED));
                addAndRecordWidth(body);
            }
            else if(string.matches("Quest Item")) {
                body = Text.translatable("wytr.tooltip.item.questItem").setStyle(Style.EMPTY.withColor(Formatting.RED));
                addAndRecordWidth(body);
            }
            else if(string.matches(".+ Item( \\[\\d+])?$")) {
                String[] splits = string.split(" ");
                Text rarity = ItemRarity.getRarity(splits[0]);
                body = Text.translatable("wytr.tooltip.item.rarity", rarity).setStyle(rarity.getStyle())
                        .append(splits.length == 3 ? " " + splits[2] : "");
                addAndRecordWidth(body);
            }
            else {
                Text lore = mergeTextStyleSide(texts.subList(i, size));
                body = Text.translatableWithFallback(itemNameKey + ".lore", lore.getString()).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY));
                tempText.add(body);
                wrapTargetIdx.add(tempText.size() - 1);
                return;
            }
        }

    }

    private void appendUnknowns(List<Text> texts) {
        texts.forEach(t -> {
            debugClass.writeTextAsJSON(t, "UnknownEquipTooltipParts");
            tempText.add(t);
        });
    }

    private void updateLongestWidth(int width) {
        longestWidth = Math.max(longestWidth, width);
    }

    private void addAndRecordWidth(Text text) {
        updateLongestWidth(TEXT_RENDERER.getWidth(text));
        tempText.add(text);
    }
}
