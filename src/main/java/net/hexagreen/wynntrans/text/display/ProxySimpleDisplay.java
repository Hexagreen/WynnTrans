package net.hexagreen.wynntrans.text.display;

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

    public ProxySimpleDisplay(Text text) {
        super(text);
        this.template = Templates.findTemplate(inputText);
    }

    @Override
    protected String initValText() {
        return Templates.findTemplate(inputText).mutator.apply(inputText).replaceFirst("^(?:§.)+", "");
    }

    @Override
    protected MutableText newTranslate(String key) {
        return Text.translatable(key, template.argumentParser.apply(inputText));
    }

    public enum Templates {
        DRAWING_OF_THE_SPIRITS_TIMER(text -> text.getString().contains("§7Return to the Seer in "),
                text -> text.getString().replaceFirst("in §.§.\\d+:\\d+:\\d+:\\d+\\n", "in %1\\$s\n"),
                text -> {
                    Matcher m = Pattern.compile("in (§.§.\\d+:\\d+:\\d+:\\d+)\\n").matcher(text.getString());
                    boolean ignore = m.find();
                    return m.group(1);
                });

        private final Predicate<Text> typeChecker;
        private final Function<Text, String> mutator;
        private final Function<Text, Object> argumentParser;

        private static Templates findTemplate(Text text) {
            return Arrays.stream(Templates.values())
                    .filter(templates -> templates.typeChecker.test(text))
                    .findFirst()
                    .orElse(null);
        }

        Templates(Predicate<Text> typeChecker, Function<Text, String> mutator, Function<Text, Object> argumentParser) {
            this.typeChecker = typeChecker;
            this.mutator = mutator;
            this.argumentParser = argumentParser;
        }

        public Predicate<Text> getTypeChecker() {
            return this.typeChecker;
        }
    }
}
