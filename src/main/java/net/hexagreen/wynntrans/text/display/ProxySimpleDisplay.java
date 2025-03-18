package net.hexagreen.wynntrans.text.display;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.types.SimpleDisplay;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxySimpleDisplay extends SimpleDisplay {
    private final Templates template;

    public static boolean typeChecker(Text text) {
        return Templates.findTemplate(text) != null;
    }

    public ProxySimpleDisplay(Text text) {
        super(text);
        this.template = Templates.findTemplate(inputText);
    }

    @Override
    protected String initValText() {
        return Templates.findTemplate(inputText).mutator.apply(inputText).replaceFirst("^(?:§.)+", "");
    }

    @Override
    protected MutableText newTranslate(String key, Object... args) {
        return Text.translatable(key, template.argumentParser.apply(inputText));
    }

    public enum Templates {
        GUILD_LEADERBOARD(text -> text.getString().contains("Leaderboard\n§7Season ends in"),
                text -> Pattern.compile("^§d§lSeason \\d+ Leaderboard\\n§7Season ends in .+\\n\\n.+\\n§eClick for Options$", Pattern.DOTALL)
                        .matcher(text.getString())
                        .replaceFirst("§d§lSeason %1\\$s Leaderboard\n§7Season ends in %2\\$s\n\n%3\\$s\n§eClick for Options"),
                text -> {
                    Matcher m = Pattern.compile("(?s)§d§lSeason (\\d+) Leaderboard\\n§7Season ends in (§b§l(\\d+ .+?))\\n\\n(.+)\\n§eClick for Options", Pattern.DOTALL)
                            .matcher(text.getString());
                    boolean ignore = m.find();
                    return new Object[]{m.group(1), ITime.translateTime(m.group(3)).setStyle(parseStyleCode(m.group(2))), m.group(4)};
                }),
        GUILD_NEXT_REWARD(text -> text.getString().matches("§d§lYour Next Rewards\\n§3At §b\\d+ SR\\n.+"),
                text -> text.getString().replaceFirst("§3At §b\\d+ SR\\n.+", "§3At %1\\$s §bSR\n%2\\$s"),
                text -> {
                    Matcher m = Pattern.compile("§3At (§b\\d+) SR\\n(.+)").matcher(text.getString());
                    boolean ignore = m.find();
                    return new String[]{m.group(1), m.group(2)};
                }),
        GUILD_SEASON_REWARD(text -> text.getString().matches("(?s)^§.Season \\d+ Banner Reward.+"),
                text -> text.getString().replaceFirst("Season \\d+ Banner Reward", "Season %1\\$s Banner Reward")
                        .replaceFirst("\\n§7the .+$", "\n§7the %2\\$s"),
                text -> {
                    Matcher m = Pattern.compile("Season (\\d+) Banner Reward\\n§7Stay in §4Top 20§7 to unlock\\n§7the (.+)").matcher(text.getString());
                    boolean ignore = m.find();
                    return new String[]{m.group(1), m.group(2)};
                }),
        DRAWING_OF_THE_SPIRITS_TIMER(text -> text.getString().contains("§7Return to the Seer in "),
                text -> text.getString().replaceFirst("in §.§.\\d+:\\d+:\\d+:\\d+\\n", "in %1\\$s\n"),
                text -> {
                    Matcher m = Pattern.compile("in (§.§.\\d+:\\d+:\\d+:\\d+)\\n").matcher(text.getString());
                    boolean ignore = m.find();
                    return new String[]{m.group(1)};
                }),
        FES_SPIRITS_MINIGAME_WELL(text -> text.getString().matches("(?s)§b§lSPIRIT WELL §3- §b.+"),
                text -> {
                    String result = text.getString().replaceFirst("SPIRIT WELL §3- §b\\d+%", "SPIRIT WELL §3- %1\\$s");
                    if(text.getString().contains("Times Damaged:")) {
                        result = result.replaceFirst("§cTimes Damaged: §f\\d+", "§cTimes Damaged: %2\\$s");
                    }
                    return result;
                },
                text -> {
                    Matcher m = Pattern.compile("§b§lSPIRIT WELL §3- (§b\\d+%)\\n(?:§cTimes Damaged: (§f\\d+)\\n)?").matcher(text.getString());
                    boolean ignore = m.find();
                    if(m.group(2) == null) return new String[]{m.group(1)};
                    return new String[]{m.group(1), m.group(2)};
                }),
        FES_SPIRITS_MINIGAME_GATE(text -> text.getString().matches("(?s)§a§lGATE SWITCH §3- §a.+"),
                text -> text.getString().replaceFirst("§a§lGATE SWITCH §3- §a\\d+%", "§a§lGATE SWITCH §3- %1\\$s"),
                text -> {
                    Matcher m = Pattern.compile("§a§lGATE SWITCH §3- (§a\\d+%)").matcher(text.getString());
                    boolean ignore = m.find();
                    return new String[]{m.group(1)};
                }),
        FES_SPIRITS_MINIGAME_GRAVE(text -> text.getString().matches("(?s)§c§l.+'s Grave\\n§3.+"),
                text -> text.getString().replaceFirst("§c§l.+'s Grave\\n§3\\[.+]\\n", "%1\\$s's Grave\n%2\\$s\n"),
                text -> {
                    Matcher m = Pattern.compile("(§c§l.+)'s Grave\\n(§3\\[§c♥§8♥♥§3])\\n").matcher(text.getString());
                    boolean ignore = m.find();
                    return new String[]{m.group(1), m.group(2)};
                }),
        FES_HEROES_PERFORMANCE_RUNNING(text -> text.getString().matches("(?s)A §dperformance §7is currently active.+"),
                text -> text.getString().replaceFirst("§a.+?§7", "%1\\$s§7"),
                text -> {
                    Matcher m = Pattern.compile("(§a.+?)§7").matcher(text.getString());
                    if(m.find()) return new String[]{m.group(1)};
                    else return new String[]{"ERROR"};
                }),
        FES_HEROES_PERFORMANCE_TIMER(text -> text.getString().matches("(?s)§7The §dnext performance §7will be in.+"),
                text -> text.getString().replaceFirst("§c.+?§7", "%1\\$s§7").replaceFirst("§a.+?§7", "%2\\$s§7"),
                text -> {
                    Matcher m = Pattern.compile("(?s)(§c.+?)§7.+(§a.+?)§7").matcher(text.getString());
                    if(m.find()) return new String[]{m.group(1), m.group(2)};
                    else return new String[]{"ERROR", "ERROR"};
                }),
        FES_HEROES_PERFORMANCE_SPECIAL(text -> text.getString().matches("(?s)§7A §d§nspecial event§7 featuring an important\\n§7guest.+"),
                text -> text.getString().replaceFirst("§c.+?§7", "%1\\$s§7"),
                text -> {
                    Matcher m = Pattern.compile("(§c.+?)§7").matcher(text.getString());
                    if(m.find()) return new String[]{m.group(1)};
                    else return new String[]{"ERROR"};
                });

        private final Predicate<Text> typeChecker;
        private final Function<Text, String> mutator;
        private final Function<Text, Object[]> argumentParser;

        private static Templates findTemplate(Text text) {
            return Arrays.stream(Templates.values())
                    .filter(templates -> templates.typeChecker.test(text))
                    .findFirst()
                    .orElse(null);
        }

        Templates(Predicate<Text> typeChecker, Function<Text, String> mutator, Function<Text, Object[]> argumentParser) {
            this.typeChecker = typeChecker;
            this.mutator = mutator;
            this.argumentParser = argumentParser;
        }
    }
}
