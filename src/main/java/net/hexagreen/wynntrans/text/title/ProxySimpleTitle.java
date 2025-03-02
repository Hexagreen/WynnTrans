package net.hexagreen.wynntrans.text.title;

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
        JOINING_QUEUE_TIMER(
                text -> {
                    if(text.getSiblings().size() != 2) return false;
                    return text.getSiblings().get(1).getString().matches("Joining queue in \\d+ seconds");
                },
                s -> s.replaceFirst("\\d+", "%s"),
                s -> s.replaceAll("\\D", "")
        );

        private final Predicate<Text> template;
        private final Function<String, String> mutator;
        private final Function<String, Object> argumentParser;

        private static Templates findTemplate(Text text) {
            return Arrays.stream(Templates.values())
                    .filter(templates -> templates.template.test(text))
                    .findFirst()
                    .orElse(null);
        }

        Templates(Predicate<Text> template, Function<String, String> mutator, Function<String, Object> argumentParser) {
            this.template = template;
            this.mutator = mutator;
            this.argumentParser = argumentParser;
        }
    }
}
