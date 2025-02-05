package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.Dungeons;
import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class ContentBookNodes extends WynnTooltipText {
    private static final Text EMPTY_LINE = Text.empty().append(Text.literal(" ")
            .setStyle(Style.EMPTY.withItalic(false).withColor(Formatting.DARK_PURPLE)));

    public static boolean typeChecker(List<Text> text) {
        Text reformed = colorCodedToStyled(text.getFirst());
        if(reformed.getSiblings().size() != 2) return false;
        return reformed.getSiblings().get(1).getString()
                .matches("\\[Quest]|\\[Mini-Quest]|\\[World Event]" +
                        "|\\[Secret Discovery]|\\[World Discovery]|\\[Territorial Discovery]" +
                        "|\\[Cave]|\\[Dungeon]|\\[Raid]|\\[Boss Altar]|\\[Lootrun Camp]");
    }

    public ContentBookNodes(List<Text> text) {
        super(colorCodedToStyledBatch(text));
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        for(Text line : processTextChunk()) {
            resultText.append(line);
        }
    }

    private List<Text> processTextChunk() {
        List<List<Text>> chunks = splitTextChunk();
        Collector collector = new Collector();
        for(int i = 0; i < chunks.size(); i++) {
            List<Text> chunk = chunks.get(i);
            if(i == 0) {
                collector.processNameSection(chunk);
            }
            else if(i == 1 && !chunk.getFirst().getString().matches("^[✔✖].+")) {
                collector.preProcessDescSection(chunk);
            }
            else if(chunk.getFirst().getString().matches("^[✔✖].+")) {
                collector.processInfoSection(chunk);
            }
            else if(chunk.getFirst().getString().contains("Rewards:")) {
                collector.processRewardSection(chunk);
            }
            else if(chunk.getFirst().getString().contains("CLICK TO")) {
                collector.processCTTSection(chunk);
            }
        }
        return collector.getResult();
    }

    private List<List<Text>> splitTextChunk() {
        List<List<Text>> result = new ArrayList<>();
        List<Text> target = new ArrayList<>(getSiblings());
        while(target.contains(EMPTY_LINE)) {
            int emptyLineIndex = target.indexOf(EMPTY_LINE);
            if(emptyLineIndex > 0) result.add(new ArrayList<>(target.subList(0, emptyLineIndex)));
            target = new ArrayList<>(target.subList(emptyLineIndex + 1, target.size()));
        }
        if(!target.isEmpty()) result.add(target);
        return result;
    }

    private class Collector implements ISpaceProvider {
        private final List<Text> result;
        private Text desc = null;
        private String baseKey;
        private String category;
        private String normalizedName;
        private int wrapWidth;

        Collector() {
            this.result = new ArrayList<>();
        }

        private List<Text> getResult() {
            return result;
        }

        private void initBaseKey() {
            switch(category) {
                case "Quest", "Mini-Quest" -> this.baseKey = "wytr.quest.";
                case "World Event" -> this.baseKey = "wytr.worldEvent.";
                case "Secret Discovery" -> this.baseKey = "wytr.discovery.";
                case "World Discovery", "Territorial Discovery" -> this.baseKey = "wytr.area.";
                case "Cave" -> this.baseKey = "wytr.cave.";
                case "Dungeon" -> this.baseKey = "wytr.dungeon.";
                case "Raid" -> this.baseKey = "wytr.raid.";
                case "Boss Altar" -> this.baseKey = "wytr.bossAltar.";
                case "Lootrun Camp" -> this.baseKey = "wytr.lootrun.";
            }
        }

        private void processNameSection(List<Text> chunk) {
            this.category = chunk.getFirst().getSiblings().getLast().getString().replaceAll("[\\[\\]]", "");
            Text name = chunk.getFirst().getSiblings().getFirst();
            this.normalizedName = normalizeStringForKey(name.getString());
            initBaseKey();
            nameSectionTitle(name);
            if(category.equals("World Event")) {
                Text eventArea = chunk.get(1);
                String areaName = eventArea.getString();
                Style areaStyle = eventArea.getSiblings().getFirst().getStyle();
                String key = "wytr.area." + normalizeStringForKey(areaName);
                if(WTS.checkTranslationExist(key, areaName)) {
                    result.add(Text.translatable(key).setStyle(areaStyle));
                }
                else {
                    result.add(eventArea);
                }
            }
            nameSectionStatus(chunk.getLast());
        }

        private void nameSectionTitle(Text name) {
            MutableText contentName = Text.empty();
            if(!category.equals("Dungeon")) {
                if(WTS.checkTranslationExist(baseKey + normalizedName, name.getString().replaceFirst(" $", ""))) {
                    contentName.append(Text.translatable(baseKey + normalizedName).setStyle(name.getStyle())).append(" ");
                }
                else {
                    contentName.append(name);
                }
            }
            else {
                contentName.append(Dungeons.getDungeons(normalizedName).getDungeonName().setStyle(name.getStyle())).append(" ");
            }
            String hash = DigestUtils.sha1Hex("- " + category);
            MutableText categoryText = Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.GRAY))
                    .append(Text.translatable("wytr.tooltip." + hash + "_2"))
                    .append("]");
            contentName.append(categoryText);

            result.add(contentName);
            result.add(name.copy().fillStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
        }

        private void nameSectionStatus(Text line) {
            String postBody = null;
            String body = line.getString();
            Style style = line.getSiblings().getFirst().getStyle();
            if(body.contains(" - ")) {
                String[] split = body.split(" - ");
                postBody = split[0];
                body = split[1];
            }
            MutableText status = Text.empty().setStyle(style);
            if(postBody != null) {
                if(category.equals("Secret Discovery")) {
                    status.append(Text.translatable(baseKey + "area." + normalizeStringForKey(postBody)));
                }
                else {
                    String key = "wytr.tooltip.contentStatus." + DigestUtils.sha1Hex(postBody).substring(0, 4);
                    if(WTS.checkTranslationExist(key, postBody)) {
                        status.append(Text.translatable(key));
                    }
                    else {
                        status.append(postBody);
                    }
                }
                status.append(" - ");
            }
            if(body.contains("Event starting in")) {
                Text time = ITime.translateTime(body.replaceFirst("Event starting in ", ""));
                status.append(Text.translatable("wytr.tooltip.contentStatus.57a2", time));
            }
            else {
                String key = "wytr.tooltip.contentStatus." + DigestUtils.sha1Hex(body).substring(0, 4);
                if(WTS.checkTranslationExist(key, body)) {
                    status.append(Text.translatable(key));
                }
                else {
                    status.append(body);
                }
            }

            result.add(status);
        }

        private void preProcessDescSection(List<Text> chunk) {
            Text merged = mergeTextStyleSide(chunk.toArray(Text[]::new));
            switch(category) {
                case "Quest", "Mini-Quest" -> descSectionQuest(merged);
                case "Secret Discovery", "World Discovery" -> descSectionDiscovery(merged);
                case "Cave" -> descSectionCave(merged);
                case "World Event", "Raid", "Boss Altar" -> descSectionOther(merged);
            }
        }

        private void descSectionQuest(Text descText) {
            MutableText reformed = Text.empty();
            List<Text> coordinates = new ArrayList<>();
            AtomicReference<StringBuilder> body = new AtomicReference<>(new StringBuilder());
            AtomicReference<Style> currentStyle = new AtomicReference<>(Style.EMPTY);
            descText.visit((style, string) -> {
                if(string.isEmpty()) return Optional.empty();
                if(string.matches("\\[-?\\d+, -?\\d+, -?\\d+]")) {
                    coordinates.add(Text.literal(string).setStyle(style));
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

            if(category.equals("Mini-Quest")) {
                List<Text> siblings = reformed.getSiblings();
                if(siblings.size() == 5) {
                    Text postName = Text.translatable("wytr.display.post.slay.req", siblings.get(3).getString().replaceAll("\\D", ""))
                            .setStyle(siblings.get(3).getStyle());
                    desc = Text.translatable("wytr.tooltip.miniQuestDesc.slay", siblings.get(1), postName, coordinates.getFirst())
                            .setStyle(Style.EMPTY.withColor(Formatting.GRAY));
                }
                else {
                    String postString = siblings.get(5).getString();
                    Text profession = Profession.getProfession(postString.replaceFirst("\\[(\\w+ing).+", "$1")).getText();
                    Text postName = Text.translatable("wytr.display.post.gather.req", profession, postString.replaceAll("\\D", ""))
                            .setStyle(siblings.get(5).getStyle());
                    desc = Text.translatable("wytr.tooltip.miniQuestDesc.gather", siblings.get(1), siblings.get(3), postName, coordinates.getFirst())
                            .setStyle(Style.EMPTY.withColor(Formatting.GRAY));
                }
                return;
            }


            boolean translateSuccess = true;
            String hash = DigestUtils.sha1Hex(reformed.getString()).substring(0, 4);
            List<Text> siblings = reformed.getSiblings();
            StringBuilder key = new StringBuilder().append(baseKey).append(normalizedName).append(".step.").append(hash);
            MutableText questDesc = Text.empty();
            Object[] args = coordinates.toArray(Text[]::new);
            if(siblings.size() == 1) {
                if(WTS.checkTranslationExist(key.toString(), reformed.getString())) {
                    if(coordinates.isEmpty()) {
                        questDesc = Text.translatable(key.toString()).setStyle(siblings.getFirst().getStyle());
                    }
                    else {
                        questDesc = Text.translatable(key.toString(), args).setStyle(siblings.getFirst().getStyle());
                    }
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
                        if(coordinates.isEmpty()) {
                            questDesc.append(Text.translatable(keyDesc).setStyle(styleDesc));
                        }
                        else {
                            questDesc.append(Text.translatable(keyDesc, args).setStyle(styleDesc));
                        }
                    }
                    else {
                        translateSuccess = false;
                    }
                }
            }
            desc = translateSuccess ? questDesc : descText.copy();
        }

        private void descSectionDiscovery(Text descText) {
            StringBuilder key = new StringBuilder().append(baseKey).append(normalizedName).append(".desc");
            Text carrierText;
            if(WTS.checkTranslationExist(key.toString(), descText.getString())) {
                carrierText = Text.translatable(key.toString());
            }
            else {
                carrierText = descText;
            }
            String carrier = carrierText.getString().replaceAll("\n", " ");
            desc = Text.literal(carrier).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        }

        private void descSectionCave(Text descText) {
            Text coordinate = Text.literal(descText.getString().replaceFirst(".+(\\[-?\\d+, -?\\d+, -?\\d+]).+", "$1"))
                    .setStyle(Style.EMPTY.withColor(Formatting.WHITE));
            Text caveDesc;
            StringBuilder key = new StringBuilder().append(baseKey).append(normalizedName).append(".desc");
            String valDesc = descText.getString().replaceAll("\\[-?\\d+, -?\\d+, -?\\d+]", "%1\\$s");
            if(WTS.checkTranslationExist(key.toString(), valDesc)) {
                caveDesc = Text.translatable(key.toString(), coordinate).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            }
            else {
                caveDesc = descText;
            }
            desc = caveDesc;
        }

        private void descSectionOther(Text descText) {
            StringBuilder key = new StringBuilder().append(baseKey).append(normalizedName).append(".desc");
            String valDesc = descText.getString();
            Text otherDesc;
            if(WTS.checkTranslationExist(key.toString(), valDesc)) {
                otherDesc = Text.translatable(key.toString()).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            }
            else {
                otherDesc = descText;
            }
            desc = otherDesc;
        }

        private void postProcessDescSection() {
            if(desc == null) return;
            result.add(Text.literal(" "));
            result.addAll(wrapLine(desc, wrapWidth));
        }

        private void processInfoSection(List<Text> chunk) {
            Style style = Style.EMPTY.withColor(Formatting.GRAY);
            List<Text> infos = new ArrayList<>();
            for(Text line : chunk) {
                String string = line.getString();
                Text checkbox = getIndentation(line);
                Text body;
                if(string.contains("Combat Lv. Min")) {
                    String num = string.replaceAll("\\D", "");
                    body = Text.translatable("wytr.requirement.combat", num).setStyle(style);
                }
                else if(string.contains("Recommended Combat Lv")) {
                    String num = string.replaceAll("\\D", "");
                    body = Text.translatable("wytr.requirement.combatRecommend", num).setStyle(style);
                }
                else if(string.contains("Quest Req")) {
                    Text require = infoSectionGetQuestReq(line);
                    body = Text.translatable("wytr.requirement.quest", require).setStyle(style);
                }
                else if(string.contains("ing Lv. Min")) {
                    Text require = infoSectionGetReqProfession(line);
                    String num = string.replaceAll("\\D", "");
                    body = Text.translatable("wytr.requirement.profession", require, num).setStyle(style);
                }
                else if(string.contains("Distance")) {
                    Text distance = infoSectionGetDistance(line);
                    body = Text.translatable("wytr.distance", distance).setStyle(style);
                }
                else if(string.contains("Length")) {
                    Text length = infoSectionGetLength(line);
                    body = Text.translatable("wytr.length", length).setStyle(style);
                }
                else if(string.contains("Difficulty")) {
                    Text difficulty = infoSectionGetDifficulty(line);
                    body = Text.translatable("wytr.difficulty", difficulty).setStyle(style);
                }
                else {
                    infos.add(line);
                    continue;
                }
                infos.add(Text.empty().append(checkbox).append(body));
            }
            int textsWidth = getLongestWidth(result);
            wrapWidth = getLongestWidth(infos, textsWidth);
            wrapWidth = Math.max(wrapWidth, 180);
            postProcessDescSection();
            result.add(Text.literal(" "));
            result.addAll(infos);
        }

        private Text infoSectionGetQuestReq(Text infoText) {
            Text questName;
            String valQuest = infoText.getSiblings().getLast()
                    .getString().replaceFirst(" ?Quest Req: ", "");
            String keyQuest = "wytr.quest." + normalizeStringForKey(valQuest);
            if(WTS.checkTranslationExist(keyQuest, valQuest)) {
                questName = Text.translatable(keyQuest);
            }
            else {
                questName = Text.literal(valQuest);
            }
            return questName;
        }

        private Text infoSectionGetDistance(Text infoText) {
            Text dist = infoText.getSiblings().get(2);
            String strDist = dist.getString().toLowerCase(Locale.ENGLISH).replace(" ", "");
            Style styleDist = dist.getStyle();
            Text distTranslated = Text.translatable("wytr.distance." + strDist).setStyle(styleDist);

            if(infoText.getSiblings().size() == 4) {
                Text blocks = infoText.getSiblings().getLast();
                String strBlocks = blocks.getString().replaceFirst(" ?\\((\\d+\\+?) Blocks\\)", "$1");
                Style styleBlocks = blocks.getStyle();
                Text blocksTranslated = Text.translatable("wytr.distance.blocks", strBlocks).setStyle(styleBlocks);
                return Text.empty().append(distTranslated).append(" ").append(blocksTranslated);
            }
            return Text.empty().append(distTranslated);
        }

        private Text infoSectionGetReqProfession(Text infoText) {
            String profName = infoText.getSiblings().getLast().getString().replaceFirst(" ?(.+ing).+", "$1");
            return Profession.getProfession(profName).getText();
        }

        private Text infoSectionGetLength(Text infoText) {
            Text length = infoText.getSiblings().get(1);
            String strLen = length.getString().replaceFirst("Length: ", "").toLowerCase(Locale.ENGLISH);
            Style styleLen = length.getStyle();
            Text lengthTranslated = Text.translatable("wytr.length." + strLen).setStyle(styleLen);

            if(infoText.getSiblings().size() == 3) {
                Text time = infoText.getSiblings().getLast();
                Style styleTime = time.getStyle();
                Text blocksTranslated = Text.empty().setStyle(styleTime).append("(")
                        .append(ITime.translateTime(time.getString().replaceAll("[()]", "")).setStyle(styleTime)).append(")");
                return Text.empty().append(lengthTranslated).append(" ").append(blocksTranslated);
            }
            return Text.empty().append(lengthTranslated);
        }

        private Text infoSectionGetDifficulty(Text infoText) {
            Text diff = infoText.getSiblings().getLast();
            String strDiff = diff.getString().replaceFirst("Difficulty: ", "").replace(" ", "").toLowerCase(Locale.ENGLISH);
            Style styleDiff = diff.getStyle();
            return Text.translatable("wytr.difficulty." + strDiff).setStyle(styleDiff);
        }

        private void processRewardSection(List<Text> chunk) {
            result.add(Text.literal(" "));
            result.add(Text.literal("   ÀÀ").append(Text.translatable("wytr.func.reward").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE))));
            List<Text> wrapped = new ArrayList<>();
            for(Text line : rewardSectionReform(chunk.subList(1, chunk.size()))) {
                List<Text> wrappedLine = wrapLine(line, wrapWidth);
                List<Text> reformed = new ArrayList<>();
                reformed.add(wrappedLine.getFirst());
                if(wrappedLine.size() != 1) {
                    for(int i = 1; wrappedLine.size() > i; i++) {
                        Text text = wrappedLine.get(i);
                        MutableText newText = Text.empty().append(" ");
                        for(Text sib : text.getSiblings()) {
                            newText.append(sib);
                        }
                        reformed.add(newText);
                    }
                }
                wrapped.addAll(reformed);
            }
            result.addAll(wrapped);
        }

        private List<Text> rewardSectionReform(List<Text> lines) {
            List<Text> rewards = new ArrayList<>();
            MutableText newLine = Text.empty();
            AtomicReference<StringBuilder> nowString = new AtomicReference<>(new StringBuilder());
            AtomicReference<Style> nowStyle = new AtomicReference<>(Style.EMPTY);
            for(Text line : lines) {
                if(line.contains(Text.literal("- ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE).withItalic(false)))) {
                    if(!nowString.get().isEmpty()) {
                        newLine.append(Text.literal(nowString.get().toString()).setStyle(nowStyle.get()));
                        rewards.add(newLine);
                        newLine = Text.empty();
                        nowString.set(new StringBuilder());
                        nowStyle.set(Style.EMPTY);
                    }
                }
                MutableText finalNewLine = newLine;
                line.visit((style, string) -> {
                    if(string.isEmpty()) return Optional.empty();
                    if(style.getFont().equals(Identifier.of("minecraft:space"))) return Optional.empty();
                    if(!style.isItalic()) style = style.withItalic(false);
                    if(!nowStyle.get().equals(style)) {
                        if(!nowString.get().isEmpty()) {
                            finalNewLine.append(Text.literal(nowString.get().toString()).setStyle(nowStyle.get()));
                            nowString.set(new StringBuilder());
                        }
                        nowStyle.set(style);
                    }
                    nowString.get().append(string);
                    return Optional.empty();
                }, Style.EMPTY);
                nowString.set(new StringBuilder(nowString.get().toString().replaceFirst(" +$", "") + " "));
            }
            if(!nowString.get().isEmpty()) {
                newLine.append(Text.literal(nowString.get().toString()).setStyle(nowStyle.get()));
                rewards.add(newLine);
            }
            return rewards.stream().map(ContentBookNodes.this::mergeTextStyleSide).map(this::rewardSectionTranslate).toList();
        }

        private Text rewardSectionTranslate(Text text) {
            String reward = text.getSiblings().get(1).getString();
            MutableText line = Text.literal(" ").setStyle(Style.EMPTY.withColor(Formatting.GRAY))
                    .append(text.getSiblings().getFirst());
            if(reward.matches("\\+\\d+(?: Combat)? XP")) {
                String num = reward.replaceAll("\\D", "");
                return line.append(Text.translatable("wytr.func.reward.xp", num));
            }
            else if(reward.matches("\\+\\d+ Emeralds?")) {
                String num = reward.replaceAll("\\D", "");
                return line.append(Text.translatable("wytr.func.reward.emerald", num));
            }
            else if(reward.matches("\\+\\d+ .+ing XP")) {
                String num = reward.replaceAll("\\D", "");
                Text prof = Profession.getProfession(reward.replaceFirst("\\+\\d+ ", "").replaceFirst(" XP", ""))
                        .getText();
                return line.append(Text.translatable("wytr.func.reward.profXp", num, prof));
            }
            else if(reward.equals("+Various Items")) {
                return line.append(Text.translatable("wytr.tooltip.contentReward.various"));
            }
            else {
                String val = text.getString().replaceFirst("^- ", "");
                String hash = DigestUtils.sha1Hex(val).substring(0, 4);
                String key;
                for(int i = 1, size = text.getSiblings().size(); i < size; i++) {
                    String subKey;
                    if(size == 2) subKey = "";
                    else {
                        subKey = "." + i;
                        val = text.getSiblings().get(i).getString();
                    }

                    switch(category) {
                        case "Quest", "Mini-Quest", "Cave" ->
                                key = baseKey + normalizedName + ".reward." + hash + subKey;
                        case "World Event", "Raid" -> key = baseKey + "reward." + hash + subKey;
                        case "Dungeon" -> {
                            String num = val.replaceAll("\\D", "");
                            Dungeons dungeon = Dungeons.getDungeons(normalizedName);
                            Text item;
                            if(val.contains("Fragments")) item = dungeon.getDungeonFragment();
                            else item = dungeon.getDungeonBossReward();
                            return line.append(Text.translatable("wytr.tooltip.contentReward.dungeon", num, item));
                        }
                        default -> key = "wytr.tooltip.contentReward." + hash + subKey;
                    }
                    if(WTS.checkTranslationExist(key, val)) {
                        line.append(Text.translatable(key));
                    }
                    else {
                        line.append(val);
                    }
                }
                return line;
            }
        }

        private void processCTTSection(List<Text> chunk) {
            Text message = chunk.getFirst().getSiblings().getLast();
            String key;
            if(message.getString().contains("UN")) key = "wytr.func.CLICK_TO_UNTRACK";
            else key = "wytr.func.CLICK_TO_TRACK";
            Text ctt = Text.translatable(key).setStyle(message.getStyle());

            int totalWidth = getLongestWidth(result);
            result.add(Text.literal(" "));
            result.add(getCenterIndent(ctt, totalWidth).append(ctt));
        }

        private Text getIndentation(Text infoText) {
            Text blank = infoText.getSiblings().getFirst();
            if(blank.getString().contains("✔")) {
                return Text.literal("✔À ").setStyle(Style.EMPTY.withColor(Formatting.GREEN));
            }
            else if(blank.getString().contains("✖")) {
                return Text.literal("✖À ").setStyle(Style.EMPTY.withColor(Formatting.RED));
            }
            else return blank;
        }
    }
}
