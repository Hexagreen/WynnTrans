package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.chat.types.SimpleText;
import net.hexagreen.wynntrans.text.tooltip.AbstractIdentifiableItem;
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

import static net.hexagreen.wynntrans.text.WynntilsCompatible.coloringDefectiveItem;
import static net.hexagreen.wynntrans.text.WynntilsCompatible.coloringPerfectItem;

public class NormalEquipment extends AbstractIdentifiableItem implements ISpaceProvider {
    private static final Pattern NAME_ATTACHMENT_REGEX =
            Pattern.compile("(Slow|Normal|Fast) Attack Speed|This item's powers have");
    private static final Pattern BASE_STAT_REGEX =
            Pattern.compile("❤ Health: |[\uE000-\uE004] .+ Defence: |[\uE000-\uE005] .+ Damage: ");
    private static final Pattern POWDER_SPECIAL_REGEX =
            Pattern.compile(" (Quake|Chain Lightning|Curse|Courage|Wind Prison|Rage|Kill Streak|Concentration|Endurance|Dodge) [IV]+(?: \\[(.+)])?$");
    private final List<Text> tempText;
    private final Deque<Integer> wrapTargetIdx;
    private final Deque<Integer> alignTargetIdx;
    protected Pattern itemTailRegex;
    private int longestWidth;
    private String itemNameKey = "";
    private boolean nameAppended = false;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() < 2) return false;
        if(Pattern.compile("[\uE000\uE001\uE002\uE004]").matcher(WynnTrans.currentScreen).find()) return false;
        if(texts.get(1).getString().contains("Sold")) return false;
        String itemName = texts.getFirst().getString().replaceAll("^Unidentified |^Perfect |^Defective |§f⬡ Shiny |\\[.+%]$", "");
        String normalized = normalizeStringForKey(itemName);
        boolean rarityFind = false;
        for(int i = texts.size() - 1; i >= 0; i--) {
            if(Pattern.compile("^(§7)?(§7)?(Normal|Unique|Rare|Legendary|Fabled|Mythic|Set) .+").matcher(texts.get(i).getString()).find()) {
                rarityFind = true;
                break;
            }
        }
        return rarityFind && (WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalized));
    }

    public static MutableText getTranslatedItemName(Text text) {
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        String itemName = text.getString().replaceAll("^Unidentified |^Perfect |^Defective |⬡ Shiny |\\[.+%]$", "");
        String normalized = normalizeStringForKey(itemName);
        if(WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalized)) {
            NormalEquipment equipment = new NormalEquipment(textBowl);
            equipment.translateNameSection(equipment.inputText.getSiblings());
            return equipment.tempText.getFirst().copy();
        }
        else if(itemName.contains(" spell cast! ") || itemName.contains("You don't have enough ")) {
            NormalEquipment equipment = new NormalEquipment(textBowl);
            return equipment.translateSkillCastedMessage(equipment.getSibling(0));
        }
        else return null;
    }

    public NormalEquipment(List<Text> texts) {
        super(texts);
        this.tempText = new ArrayList<>();
        this.wrapTargetIdx = new ArrayDeque<>();
        this.alignTargetIdx = new ArrayDeque<>();
        this.longestWidth = 0;
        this.itemTailRegex = Pattern.compile(".+ Item( \\[\\d+])?$|Powder Slots");
        this.textAppender = this::addAndRecordWidth;
        this.optionalTextAppender = this::addAndMarkWrapTarget;
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void buildStep(List<Text> seg, int step, final int lastIndex) {
        String firstLine = seg.getFirst().getString();
        String firstSibling = seg.getFirst().getSiblings().getFirst().getString();
        if(step == 0) {
            translateNameSection(seg);
        }
        else if(NAME_ATTACHMENT_REGEX.matcher(firstLine).find()) {
            translateNameSection(seg);
        }
        else if(BASE_STAT_REGEX.matcher(firstLine).find()) {
            translateBaseStatSection(seg);
        }
        else if(POWDER_SPECIAL_REGEX.matcher(firstLine).find()) {
            translatePowderSpecialSection(seg);
        }
        else if(firstLine.contains("Lending Price")) {
            translateLendingPriceSection(seg);
        }
        else if(firstSibling.matches("[✔✖] ?")) {
            translateReqSection(seg);
        }
        else if(firstSibling.equals("⬡ ")) {
            translateShinySection(seg);
        }
        else if(firstSibling.matches("[+-]\\d+(%|/[35]s| tier)?")) {
            translateIDSection(seg);
        }
        else if(firstSibling.matches("^\\+.+: $")) {
            translateIDSection(seg);
        }
        else if(firstLine.matches(".+ Set \\(\\d/\\d\\)")) {
            translateSetPartsSection(seg);
        }
        else if(firstLine.equals("Set Bonus:")) {
            translateSetBonusSection(seg);
        }
        else if(itemTailRegex.matcher(firstLine).find()) {
            translateTailSection(seg);
        }
        else {
            appendUnknowns(seg);
        }
        if(step != lastIndex) tempText.add(SPLITTER);
        else {
            wrapTargetIdx.add(-1);
            alignTargetIdx.add(-1);

            for(int j = 0, size = tempText.size(); j < size; j++) {
                if(j == wrapTargetIdx.getFirst()) {
                    wrapLine(tempText.get(j), Math.max(longestWidth, 150)).forEach(resultText::append);
                    wrapTargetIdx.removeFirst();
                }
                else if(j == alignTargetIdx.getFirst()) {
                    resultText.append(centerAlign(tempText.get(j), longestWidth));
                    alignTargetIdx.removeFirst();
                }
                else {
                    resultText.append(tempText.get(j));
                }
            }
        }
    }

    @Override
    protected void postStep(List<Text> seg, int i, int lastIndex) {
        if(i == lastIndex) {
            wrapTargetIdx.add(-1);
            alignTargetIdx.add(-1);

            for(int j = 0, size = tempText.size(); j < size; j++) {
                if(j == wrapTargetIdx.getFirst()) {
                    wrapLine(tempText.get(j), Math.max(longestWidth, 150)).forEach(resultText::append);
                    wrapTargetIdx.removeFirst();
                }
                else if(j == alignTargetIdx.getFirst()) {
                    resultText.append(centerAlign(tempText.get(j), longestWidth));
                    alignTargetIdx.removeFirst();
                }
                else {
                    resultText.append(tempText.get(j));
                }
            }
        }
    }

    private void translateNameSection(List<Text> texts) {
        for(Text t : texts) {
            String string = t.getString().replaceAll("À", "");
            String itemNameString = string.replaceAll("^Unidentified |^Perfect |^Defective |⬡ Shiny | \\[.+%]$", "");
            String normalized = normalizeStringForKey(itemNameString);

            if(nameAppended) {
                if(normalized.equals("Thisitemspowershave")) {
                    addAndMarkWrapTarget(Text.translatable("wytr.tooltip.equipment.identifyGuide").setStyle(GRAY));
                    break;
                }
                else if(normalized.matches(".+AttackSpeed$")) {
                    Text speed = Text.translatable("wytr.speed." + normalized.replaceFirst("AttackSpeed", ""));
                    addAndRecordWidth(Text.translatable("wytr.tooltip.attackSpeed", speed).setStyle(GRAY));
                    continue;
                }
                else if(normalized.matches("Fromchat")) {
                    addAndRecordWidth(Text.translatable("wytr.func.wynntilsCompat.sharedGearStat.fromChat").formatted(Formatting.ITALIC, Formatting.DARK_GRAY));
                    continue;
                }
                else addAndRecordWidth(t);
            }

            boolean isCraftedItem = Objects.equals(t.getSiblings().getFirst().getStyle().getColor(), TextColor.fromFormatting(Formatting.DARK_AQUA));
            boolean shiny = !isCraftedItem && string.matches(".*⬡ Shiny.+");
            boolean unidentified = !isCraftedItem && string.matches("^Unidentified .+");
            boolean wynntilsPerfect = !isCraftedItem && string.matches("^Perfect .+");
            boolean wynntilsDefective = !isCraftedItem && string.matches("^Defective .+");
            boolean itemPercentage = string.matches(".+ \\[.+%]$");

            MutableText itemName = Text.empty();
            if(shiny) itemName.append(Text.translatable("wytr.tooltip.equipment.shiny"));

            if(WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalized)) {
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

            if(itemPercentage) translated.append(t.getSiblings().getLast());

            nameAppended = true;
            addAndRecordWidth(translated);
        }
    }

    private void translateBaseStatSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        texts.forEach(t -> {
            String[] split = t.getString().split(": ");
            String[] icon = split[0].split(" ");
            Text formattedIcon = Text.literal(icon[0]).setStyle(t.getSiblings().getFirst().getStyle());
            Style headStyle = t.getSiblings().getFirst().getStyle().withFont(Identifier.of("minecraft:default"));
            Text line;
            if(split[0].contains("Health")) {
                line = Text.empty().setStyle(headStyle)
                        .append("À").append(formattedIcon).append(" ")
                        .append(Text.translatable("wytr.tooltip.baseStat.health"))
                        .append(": " + split[1]);
            }
            else if(split[0].contains("Neutral")) {
                Text neutral = Text.empty().append(formattedIcon).append(" ").append(Text.translatable("wytr.element.neutral"));
                line = Text.translatable("wytr.tooltip.baseStat.damage", neutral).setStyle(headStyle)
                        .append(": " + split[1]);
            }
            else if(split[0].contains("DPS")) {
                line = Text.empty().append(t.getSiblings().getFirst()).formatted(Formatting.DARK_GRAY)
                        .append(Text.translatable("wytr.tooltip.baseStat.dps"))
                        .append(": ").append(Text.literal(split[1]).formatted(Formatting.GRAY));
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

                Text element = Text.empty().setStyle(headStyle)
                        .append(formattedIcon).append(" ")
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

    private void translateLendingPriceSection(List<Text> texts) {
        for(Text t : texts) {
            String _s = t.getString();
            if(_s.contains("Lending Price")) {
                tempText.add(Text.translatable("wytr.tooltip.lending").formatted(Formatting.GOLD));
            }
            else if(_s.contains("Credits")) {
                Text cost = t.getSiblings().get(1);
                tempText.add(Text.translatable("wytr.tooltip.lending.cost", cost).formatted(Formatting.GRAY));
            }
            else {
                tempText.add(t);
            }
            alignTargetIdx.add(tempText.size() - 1);
        }
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

    private void translateSetPartsSection(List<Text> texts) {
        List<Text> siblings = texts.getFirst().getSiblings();
        String setNameVal = siblings.getFirst().getString().replaceFirst(" Set $", "");
        String setNameKey = "wytr.setItem." + normalizeStringForKey(setNameVal);
        WTS.checkTranslationExist(setNameKey, setNameVal);
        Text setName = Text.translatableWithFallback(setNameKey, setNameVal);
        Text head = Text.translatable("wytr.tooltip.equipment.setFamily", setName).setStyle(siblings.getFirst().getStyle())
                .append(" ").append(siblings.getLast());
        addAndRecordWidth(head);
        texts.subList(1, texts.size()).forEach(t -> {
            Text itemName = t.getSiblings().getLast();
            MutableText line = t.getSiblings().getFirst().copy()
                    .append(new ItemName(itemName).noAddictionMode().textAsMutable().setStyle(itemName.getStyle()));
            addAndRecordWidth(line);
        });
    }

    private void translateSetBonusSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        dump.add(Text.translatable("wytr.tooltip.equipment.setBonus").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        dump.addAll(new IdentificationTooltip(texts.subList(1, texts.size())).text());
        dump.forEach(this::addAndRecordWidth);
    }

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
                addAndMarkWrapTarget(body);
                return;
            }
        }
    }

    private MutableText translateSkillCastedMessage(Text text) {
        if(text.getString().contains("You don't have enough ")) {
            return new SimpleText(text.getSiblings().getFirst()).text().setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
        }
        Deque<Text> siblings = new ArrayDeque<>(text.getSiblings());
        String _skillName = siblings.removeFirst().getString().replaceFirst(" spell cast! ", "");
        Text skillName = Text.translatable("wytr.ability.node." + normalizeStringForKey(_skillName));
        MutableText line = Text.empty().setStyle(GRAY);
        line.append(Text.translatable("wytr.func.skillCasted", skillName).setStyle(GRAY));
        line.append(" ");
        siblings.forEach(line::append);
        return line;
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

    protected void addAndRecordWidth(Text text) {
        updateLongestWidth(WynnTransText.TEXT_RENDERER.getWidth(text));
        tempText.add(text);
    }

    protected void addAndMarkWrapTarget(Text text) {
        tempText.add(text);
        wrapTargetIdx.add(tempText.size() - 1);
    }
}
