package net.hexagreen.wynntrans.text.title;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.title.types.SimpleTitle;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public class ProxySimpleTitle extends SimpleTitle {
    private final Templates template;

    public static boolean typeChecker(Text text) {
        return Templates.findTemplate(text) != null;
    }

    public ProxySimpleTitle(Text text) {
        super(text);
        this.template = Templates.findTemplate(inputText);
    }

    @Override
    protected String getValText(String string) {
        return Templates.findTemplate(inputText).mutator.apply(string);
    }

    @Override
    protected MutableText newTranslate(String key) {
        return Text.translatable(key, template.argumentParser.apply(inputText.getString()));
    }

    private enum Templates {
        WORLD_JOIN_QUEUE(
                text -> {
                    if(!text.getSiblings().isEmpty()) return false;
                    return text.getString().matches("§aQueueing for (NA|EU|AS)\\d+\\.*");
                },
                s -> s.replaceFirst("(NA|EU|AS)\\d+", "%s").replaceFirst("\\.*$", "%s"),
                s -> {
                    String partial = s.replaceFirst("§aQueueing for ", "");
                    String server = partial.replaceAll("\\.", "");
                    String dot = partial.replaceAll("[^.]", "");
                    return new Object[]{server, dot};
                }
        ),
        WORLD_JOIN_QUEUE_POS(
                text -> {
                    if(!text.getSiblings().isEmpty()) return false;
                    return text.getString().matches("You are in position #\\d+");
                },
                s -> s.replaceFirst("\\d+", "%s"),
                s -> new Object[]{s.replaceAll("\\D", "")}
        ),
        JOINING_QUEUE_TIMER(
                text -> {
                    if(text.getSiblings().size() != 2) return false;
                    return text.getSiblings().get(1).getString().matches("Joining queue in \\d+ seconds");
                },
                s -> s.replaceFirst("\\d+", "%s"),
                s -> new Object[]{s.replaceAll("\\D", "")}
        ),
        WORLD_EVENT_START_TIMER(
                text -> {
                    if(!text.getSiblings().isEmpty()) return false;
                    return text.getString().matches("Starts in (\\d+[hms] ?)+");
                },
                s -> "Starts in %s",
                s -> new Object[]{ITime.translateTime(s.replaceFirst("Starts in ", ""))}
        ),
        WORLD_EVENT_NEXT_WAVE(
                text -> {
                    if(!text.getSiblings().isEmpty()) return false;
                    return text.getString().matches("Next wave in \\d");
                },
                s -> "Next wave in %s",
                s -> new Object[]{s.replaceFirst("Next wave in ", "")}
        );

        private final Predicate<Text> template;
        private final Function<String, String> mutator;
        private final Function<String, Object[]> argumentParser;

        private static Templates findTemplate(Text text) {
            return Arrays.stream(Templates.values())
                    .filter(templates -> templates.template.test(text))
                    .findFirst()
                    .orElse(null);
        }

        Templates(Predicate<Text> template, Function<String, String> mutator, Function<String, Object[]> argumentParser) {
            this.template = template;
            this.mutator = mutator;
            this.argumentParser = argumentParser;
        }
    }
}
