package net.hexagreen.wynntrans.text.scoreboard.types;

import net.hexagreen.wynntrans.enums.Dungeons;
import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.scoreboard.WynnScoreboardText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ContentTracking extends WynnScoreboardText {
    private static final Style style = Style.EMPTY.withColor(Formatting.YELLOW);
    private String category;
    private String baseKey;
    private String normalizedName;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() <= 1) return false;
        return texts.getFirst().getString().matches("§7§e§lTracked .+:");
    }

    public ContentTracking(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "scoreboard.contentTrack";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        List<Text> texts = new ArrayList<>(getSiblings());
        Text trackedTarget = getTrackedTarget(texts.removeFirst());
        Text head = Text.translatable(translationKey, trackedTarget).setStyle(style.withBold(true));
        resultText.append(head);

        Text targetName = texts.removeFirst();
        String[] split = targetName.getString().split(" \\(");
        MutableText contentName;

        this.normalizedName = normalizeStringForKey(split[0]);
        if(!category.equals("Dungeon")) {
            contentName = Text.translatableWithFallback(baseKey + normalizedName, split[0])
                    .setStyle(targetName.getStyle());
        }
        else {
            contentName = Dungeons.getDungeons(split[0]).getDungeonName().setStyle(targetName.getStyle());
        }
        if(split.length > 1) {
            Text timer = ITime.translateTime(split[1].replace(" left)", ""));
            contentName.append(" ").append(Text.translatable(translationKey + ".timer", timer));
        }
        resultText.append(contentName);

        if(texts.isEmpty()) return;


        Text desc = mergeTextStyleSide(texts);
        Text translatedDesc = descSection(desc);

        wrapLine(translatedDesc).forEach(resultText::append);
    }

    private Text getTrackedTarget(Text head) {
        String target = head.getString().replaceAll("Tracked |:", "");
        String key = "wytr.content." + normalizeStringForKey(target);
        initBaseKey(target);
        WTS.checkTranslationExist(key, target);
        return Text.translatableWithFallback(key, target);
    }

    private void initBaseKey(String category) {
        this.category = category;
        switch(category) {
            case "Quest" -> this.baseKey = "wytr.quest.";
            case "World Event" -> this.baseKey = "wytr.worldEvent.";
            case "Discovery" -> this.baseKey = "wytr.area.";
            case "Cave" -> this.baseKey = "wytr.cave.";
            case "Dungeon" -> this.baseKey = "wytr.dungeon.";
            case "Raid" -> this.baseKey = "wytr.raid.";
            case "Boss Altar" -> this.baseKey = "wytr.bossAltar.";
            case "Lootrun Camp" -> this.baseKey = "wytr.lootrun.";
        }
    }

    private MutableText descSection(Text desc) {
        switch(category) {
            case "Quest" -> {
                return descSectionQuest(desc);
            }
            case "Cave" -> {
                return descSectionCave(desc);
            }
            case "World Event", "Boss Altar" -> {
                return descSectionOther(desc);
            }
        }
        return desc.copy();
    }

    private MutableText descSectionQuest(Text descText) {
        MutableText reformed = Text.empty();
        List<Text> arguments = new ArrayList<>();
        AtomicReference<StringBuilder> body = new AtomicReference<>(new StringBuilder());
        AtomicReference<Style> currentStyle = new AtomicReference<>(Style.EMPTY);
        descText.visit((style, string) -> {
            if(string.isEmpty()) return Optional.empty();
            if(string.matches("\\[-?\\d+, -?\\d+, -?\\d+]")) {
                arguments.add(Text.literal(string).setStyle(style));
                body.get().append("%s");
                return Optional.empty();
            }
            else if(string.matches("\\[(\\d+) (.+)]")) {
                String[] split = string.replaceAll("[\\[\\]]", "").split(" ", 2);
                Text itemName = new ItemName(split[1]).textAsMutable();
                arguments.add(Text.translatable("wytr.func.questingItem", split[0], itemName).setStyle(style));
                body.get().append("%s");
                return Optional.empty();
            }
            if(!currentStyle.get().equals(style) && !body.get().isEmpty()) {
                reformed.append(Text.literal(body.toString()).setStyle(currentStyle.get()));
                body.set(new StringBuilder());
            }
            currentStyle.set(style);
            body.get().append(string);
            return Optional.empty();
        }, Style.EMPTY);
        if(!body.get().isEmpty()) {
            reformed.append(Text.literal(body.toString()).setStyle(currentStyle.get()));
        }

        if(normalizedName.matches("^Gather.+|^Slay.+")) {
            List<Text> siblings = reformed.getSiblings();
            if(siblings.getFirst().getString().contains("Slaying")) {
                Text postName = Text.translatable("wytr.display.post.slay.req", siblings.get(1).getString().replaceAll("\\D", ""))
                        .setStyle(siblings.get(1).getStyle());
                arguments.add(1, postName);
                return Text.translatable("wytr.tooltip.miniQuestDesc.slay", arguments.toArray(Object[]::new))
                        .setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            }
            else {
                String postString = siblings.get(1).getString();
                Text profession = Profession.getProfession(postString.replaceFirst("\\[(\\w+ing).+", "$1")).getText();
                Text postName = Text.translatable("wytr.display.post.gather.req", profession, postString.replaceAll("\\D", ""))
                        .setStyle(siblings.get(1).getStyle());
                arguments.add(2, postName);
                return Text.translatable("wytr.tooltip.miniQuestDesc.gather", arguments.toArray(Object[]::new))
                        .setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            }
        }

        boolean translateSuccess = true;
        String hash = DigestUtils.sha1Hex(reformed.getString()).substring(0, 4);
        List<Text> siblings = reformed.getSiblings();
        String key = baseKey + normalizedName + ".step." + hash;
        MutableText questDesc = Text.empty();
        Object[] args = arguments.toArray(Text[]::new);
        if(siblings.size() == 1) {
            if(WTS.checkTranslationExist(key, reformed.getString())) {
                questDesc = Text.translatable(key, args).setStyle(siblings.getFirst().getStyle());
            }
            else {
                translateSuccess = false;
            }
        }
        else {
            questDesc = Text.empty();
            int i = 1;
            for(Text sibling : siblings) {
                String keyDesc = key + "." + i++;
                String valDesc = sibling.getString();
                Style styleDesc = sibling.getStyle();
                if(WTS.checkTranslationExist(keyDesc, valDesc)) {
                    questDesc.append(Text.translatable(keyDesc, args).setStyle(styleDesc));
                }
                else {
                    translateSuccess = false;
                }
            }
        }
        return translateSuccess ? questDesc : descText.copy();
    }

    private MutableText descSectionCave(Text descText) {
        Text coordinate = Text.literal(descText.getString().replaceFirst(".+(\\[-?\\d+, -?\\d+, -?\\d+]).+", "$1"))
                .setStyle(Style.EMPTY.withColor(Formatting.WHITE));
        MutableText caveDesc;
        String key = baseKey + normalizedName + ".desc";
        String valDesc = descText.getString().replaceAll("\\[-?\\d+, -?\\d+, -?\\d+]", "%1\\$s");
        if(WTS.checkTranslationExist(key, valDesc)) {
            caveDesc = Text.translatable(key, coordinate).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        }
        else {
            caveDesc = descText.copy();
        }
        return caveDesc;
    }

    private MutableText descSectionOther(Text descText) {
        String key = baseKey + normalizedName + ".desc";
        String valDesc = descText.getString();
        MutableText otherDesc;
        if(WTS.checkTranslationExist(key, valDesc)) {
            otherDesc = Text.translatable(key).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        }
        else {
            otherDesc = descText.copy();
        }

        return otherDesc;
    }
}
