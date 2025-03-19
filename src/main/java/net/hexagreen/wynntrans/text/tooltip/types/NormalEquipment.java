package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.IWynntilsFeature;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class NormalEquipment extends WynnTooltipText {
    private static final Text WRONG_SPLITTER = Text.literal("§7");
    private static final Text SPLITTER = Text.literal(" ");
    private static final Style GRAY = Style.EMPTY.withColor(Formatting.GRAY);
    private static final Pattern BASE_STAT_REGEX =
            Pattern.compile("❤ Health: |[✤✦❉✹❋] .+ Defence: |[✣✤✦❉✹❋] .+ Damage: ");
    private static final Pattern POWDER_SPECIAL_REGEX =
            Pattern.compile(" (.+) [IV]+");
    private final TextRenderer textRenderer;
    private final List<Text> tempText;
    private final List<Integer> wrapTargetIdx;
    private int longestWidth;
    private String itemNameKey = "";

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        String itemName = texts.getFirst().getString().replaceAll("^Unidentified |^Perfect |^Defective | \\[.+%]$", "");
        String normalized = normalizeStringForKey(itemName);
        return WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalized) ||
                WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalized);
    }

    private static List<Text> correctSplitter(List<Text> texts) {
        return texts.stream()
                .map(text -> text.getString().equals(WRONG_SPLITTER.getString()) || text.getString().isEmpty() ? SPLITTER : text)
                .toList();
    }

    public NormalEquipment(List<Text> texts) {
        super(correctSplitter(texts));
        this.textRenderer = MinecraftClient.getInstance().textRenderer;
        this.tempText = new ArrayList<>();
        this.wrapTargetIdx = new ArrayList<>();
        this.longestWidth = 150;
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<List<Text>> segments = new ArrayList<>();
        List<Text> segment = new ArrayList<>();

        for(Text text : getSiblings()) {
            if(text.getString().equals(SPLITTER.getString())) {
                if(segment.isEmpty()) continue;
                segments.add(segment);
                segment = new ArrayList<>();
            }
            else {
                segment.add(text);
            }
        }
        if(!segment.isEmpty()) segments.add(segment);

        for(int i = 0, size = segments.size(); i < size; i++) {
            if(i == 0) {
                translateNameSection(segments.getFirst());
                tempText.add(SPLITTER);
                continue;
            }
            List<Text> seg = segments.get(i);
            if(seg.getFirst().getSiblings().getFirst().getString().equals("Price:")) {
                translatePriceSection(seg);
            }
            else if(Pattern.compile("Attack Speed|This item's powers have").matcher(seg.getFirst().getString()).find()) {
                translateNameSection(seg);
            }
            else if(BASE_STAT_REGEX.matcher(seg.getFirst().getString()).find()) {
                translateBaseStatSection(seg);
            }
            else if(POWDER_SPECIAL_REGEX.matcher(seg.getFirst().getSiblings().getLast().getString()).find()) {
                translatePowderSkillSection(seg);
            }
            else if(seg.getFirst().getSiblings().getFirst().getString().matches("[✔✖]")) {
                translateReqSection(seg);
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
            else if(Pattern.compile(".+ Item( \\[\\d+])?$|Powder Slots").matcher(seg.getFirst().getString()).find()) {
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
            String itemNameString = t.getString().replaceAll("^Unidentified |^Perfect |^Defective | \\[.+%]$", "");
            String normalized = normalizeStringForKey(itemNameString);
            boolean unidentified = t.getString().matches("^Unidentified .+");
            boolean wynntilsPerfect = t.getString().matches("^Perfect .+");
            boolean wynntilsDefective = t.getString().matches("^Defective .+");
            boolean wynntilsPercentage = t.getString().matches(".+ \\[.+%]$");
            MutableText itemName;
            if(normalized.equals("Thisitemspowershave")) {
                tempText.add(Text.translatable("wytr.tooltip.identifyGuide").setStyle(GRAY));
                wrapTargetIdx.add(tempText.size() - 1);
                break;
            }
            else if(normalized.matches(".+AttackSpeed$")) {
                addAndRecordWidth(Text.translatable("wytr.tooltip.attackSpeed." + normalized.replaceFirst("AttackSpeed", "")).setStyle(GRAY));
                continue;
            }
            else if(WTS.checkTranslationDoNotRegister("wytr.item.weapon." + normalized)) {
                itemNameKey = "wytr.item.weapon." + normalized;
                itemName = Text.translatable(itemNameKey);
            }
            else if(WTS.checkTranslationDoNotRegister("wytr.item.armour." + normalized)) {
                itemNameKey = "wytr.item.armour." + normalized;
                itemName = Text.translatable(itemNameKey);
            }
            else if(WTS.checkTranslationDoNotRegister("wytr.item.accessory." + normalized)) {
                itemNameKey = "wytr.item.accessory." + normalized;
                itemName = Text.translatable(itemNameKey);
            }
            else {
                itemName = t.getSiblings().getFirst().copy();
            }

            MutableText translated;
            if(unidentified) {
                translated = Text.translatable("wytr.tooltip.unidentified", itemName)
                        .setStyle(t.getSiblings().getFirst().getStyle());
            }
            else if(wynntilsPerfect) {
                Text perfect = Text.translatable("wytr.tooltip.perfectID", itemName);
                translated = IWynntilsFeature.coloringPerfectItem(perfect);
            }
            else if(wynntilsDefective) {
                Text defective = Text.translatable("wytr.tooltip.defectiveID", itemName);
                translated = IWynntilsFeature.coloringDefectiveItem(defective);
            }
            else {
                translated = itemName.copy().setStyle(t.getSiblings().getFirst().getStyle());
            }

            if(wynntilsPercentage) {
                translated.append(t.getSiblings().getLast());
            }

            addAndRecordWidth(translated);
            addAndRecordWidth(Text.literal(itemNameString).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        }
    }

    private void translatePriceSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        texts.forEach(t -> {
            if(t.getString().equals("Price:")) {
                dump.add(Text.translatable("wytr.func.price").setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
            }
            else {
                dump.add(t);
            }
        });
        dump.forEach(this::addAndRecordWidth);
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

    private void translatePowderSkillSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>(texts);
        dump.forEach(this::addAndRecordWidth);
    }

    private void translateReqSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        texts.forEach(t -> {
            String[] headAndNum = t.getSiblings().getLast().getString().split(": ");
            Text icon = t.getSiblings().getFirst();
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
                    .append(icon).append(" ").append(body);
            dump.add(line);
        });

        dump.forEach(this::addAndRecordWidth);
    }

    private void translateIDSection(List<Text> texts) {
        List<Text> translatedIDs = new IdentificationTooltip(texts).text();
        translatedIDs.forEach(t -> {
            tempText.add(t);
            if(t.getContent().toString().equals("empty")) {
                updateLongestWidth(textRenderer.getWidth(t));
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
        Text head = Text.translatable("wytr.tooltip.setFamily", setName).setStyle(siblings.getFirst().getStyle())
                .append(" ").append(siblings.getLast());
        addAndRecordWidth(head);
        texts.subList(1, texts.size()).forEach(this::addAndRecordWidth);
    }

    private void translateSetBonusSection(List<Text> texts) {
        List<Text> dump = new ArrayList<>();
        dump.add(Text.translatable("wytr.tooltip.setBonus").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
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
                body = Text.translatable("wytr.tooltip.powderSlot", powderInfo[0], powderInfo.length == 2 ? powderInfo[1] : "").setStyle(GRAY);
                addAndRecordWidth(body);
            }
            else if(string.matches("Untradable Item")) {
                body = Text.translatable("wytr.tooltip.untradable").setStyle(Style.EMPTY.withColor(Formatting.RED));
                addAndRecordWidth(body);
            }
            else if(string.matches(".+ Item( \\[\\d+])?$")) {
                String[] splits = string.split(" ");
                Text rarity = ItemRarity.getRarity(splits[0]);
                body = Text.translatable("wytr.tooltip.itemRarity", rarity).setStyle(rarity.getStyle())
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
        updateLongestWidth(textRenderer.getWidth(text));
        tempText.add(text);
    }
}
