package net.hexagreen.wynntrans.text.scoreboard.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.scoreboard.WynnScoreboardText;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class Lootrun extends WynnScoreboardText implements ITooltipSplitter {
    private static final Style style = Style.EMPTY.withColor(Formatting.YELLOW);

    public static boolean typeChecker(List<Text> texts) {
        if(texts.size() <= 1) return false;
        return texts.getFirst().getString().matches("§e§lLootrun:");
    }

    public Lootrun(List<Text> texts) {
        super(texts);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "scoreboard.lootrun";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Deque<Deque<Text>> segments = new ArrayDeque<>();
        splitTooltipToSegments(getSiblings(), segments, ArrayDeque::new);
        for(Deque<Text> texts : segments) {
            if(texts.getFirst().getString().equals("Lootrun:")) {
                texts.removeFirst();
                Text head = Text.translatable(translationKey).setStyle(style.withBold(true));
                resultText.append(head);

                translateInst(texts.removeFirst());
            }
            else {
                Text missionName = texts.removeFirst();
                String missionKey = "wytr.lootrun.mission." + normalizeStringForKey(missionName.getString());
                resultText.append(Text.translatableWithFallback(missionKey, missionName.getString().replace(":", ""))
                        .setStyle(missionName.getSiblings().getFirst().getStyle()).append(":"));
            }

            extractUniqueLines(texts).stream()
                    .map(this::mergeTextStyleSide)
                    .map(this::translateLine)
                    .forEach(resultText::append);

            resultText.append(SPLITTER);
        }
    }

    private void translateInst(Text text) {
        String string = text.getString();
        if(string.equals("Prepare for running...")) {
            resultText.append(Text.translatable(translationKey + ".prepareRun").formatted(Formatting.GOLD));
        }
        else if(string.equals("Choose a beacon!")) {
            resultText.append(Text.translatable(translationKey + ".chooseBeacon").formatted(Formatting.GOLD));
        }
        else if(string.equals("Preparing Challenge")) {
            resultText.append(Text.translatable(translationKey + ".prepareChallenge").formatted(Formatting.GOLD));
        }
        else if(string.equals("Collect your rewards!")) {
            resultText.append(Text.translatable(translationKey + ".collectReward").formatted(Formatting.GOLD));
        }
        else if(string.equals("You have failed a challenge!")) {
            resultText.append(Text.translatable(translationKey + ".challengeFailed").formatted(Formatting.GOLD));
        }
        else if(string.equals("Warping back to camp!")) {
            resultText.append(Text.translatable(translationKey + ".endRun").formatted(Formatting.GOLD));
        }
        else if(string.equals("Destroy the objective!")) {
            resultText.append(Text.translatable(translationKey + ".destroy").formatted(Formatting.GOLD));
        }
        else if(string.matches("Loot \\d+/\\d+ chests!")) {
            String[] nums = string.replaceFirst(".+(\\d+/\\d+).+", "$1").split("/");
            resultText.append(Text.translatable(translationKey + ".spelunk", nums[0], nums[1]).formatted(Formatting.GOLD));
        }
        else if(string.matches("Defend for \\d+s!")) {
            Text time = ITime.translateTime(text.getSiblings().get(1));
            resultText.append(Text.translatable(translationKey + ".defend", time).formatted(Formatting.GOLD));
        }
        else if(string.matches("Slay! Wave \\d+ - \\d+ Mobs? Left!")) {
            String waveNum = text.getSiblings().getFirst().getString().replaceAll("\\D", "");
            Text remain = text.getSiblings().get(1);
            resultText.append(Text.translatable(translationKey + ".wave", waveNum, remain).formatted(Formatting.GOLD));
        }
        else if(string.matches("Slay! Wave \\d+ - \\d+ Target Left!")) {
            String waveNum = text.getSiblings().getFirst().getString().replaceAll("\\D", "");
            Text remain = text.getSiblings().get(1);
            resultText.append(Text.translatable(translationKey + ".target", waveNum, remain).formatted(Formatting.GOLD));
        }
        else {
            debugClass.writeTextAsJSON(text, "LootrunScoreboard");
            resultText.append(text);
        }
    }

    private Text translateLine(Text text) {
        if(!text.getString().matches("^- .+")) throw new TextTranslationFailException("LootrunScoreboard");

        List<Text> siblings = text.getSiblings();
        MutableText result = Text.literal("- ").setStyle(siblings.getFirst().getStyle());
        String string = text.getString();
        if(string.contains("Time Left: ")) {
            Text timer = siblings.get(2);
            Text bonus;
            if(siblings.size() == 4) {
                Text _bonus = siblings.get(3);
                String bonusTime = _bonus.getString().replaceAll(" ?\\[[+-]|]", "");
                String[] bracket = _bonus.getString().trim().split(bonusTime);
                bonus = Text.empty().setStyle(_bonus.getStyle()).append(bracket[0])
                        .append(ITime.translateTime(bonusTime)).append(bracket[1]);
            }
            else bonus = Text.empty();
            result.append(Text.translatable(translationKey + ".leftTime", timer, bonus).formatted(Formatting.GRAY));
        }
        else if(string.contains("Challenges: ")) {
            Text success = siblings.get(2);
            Text slash = siblings.size() >= 4 ? siblings.get(3) : Text.empty();
            Text left = siblings.size() >= 5 ? siblings.get(4) : Text.empty();
            Text counter = Text.empty().append(success).append(slash).append(left);
            result.append(Text.translatable(translationKey + ".leftChallenge", counter).formatted(Formatting.GRAY));
        }
        else if(string.matches(".+ \\d+/\\d+m? .+")) {
            Text counter;
            if(siblings.size() <= 2) {
                counter = Text.literal(string.replaceFirst(".+ (\\d+/\\d+)m? .+", "$1"));
            }
            else {
                counter = Text.literal(siblings.get(2).getString().replaceFirst("m", "")).setStyle(siblings.get(2).getStyle());
            }
            String mission = text.getString().replaceFirst("^- ", "").replaceFirst("\\d+/\\d+", "%s");
            String missionKey = translationKey + ".missionObjective." + normalizeStringForKey(mission.replaceAll("%s", ""));
            if(WTS.checkTranslationExist(missionKey, mission)) {
                result.append(Text.translatable(missionKey, counter).setStyle(siblings.getLast().getStyle()));
            }
            else {
                return text;
            }
        }
        else {
            debugClass.writeTextAsJSON(text, "LootrunScoreboard");
            return text;
        }
        return result;
    }
}
