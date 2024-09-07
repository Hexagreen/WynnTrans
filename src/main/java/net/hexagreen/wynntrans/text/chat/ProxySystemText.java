package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.types.SimpleSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxySystemText extends SimpleSystemText {
    private final Templates template;

    public ProxySystemText(Text text, Pattern ignore) {
        super(text, ignore);
        this.template = Templates.findTemplate(inputText);
    }

    @Override
    protected String initValText() {
        return lineFeedReplacer(Templates.findTemplate(inputText).mutator.apply(inputText));
    }

    @Override
    protected MutableText newTranslateWithSplit(String key) {
        return newTranslate(key, splitter, template.argumentParser.apply(inputText));
    }

    private enum Templates {
        ANNIHILATION(Pattern.compile("^Prepare to defend the province at"),
                text -> text.getString().replaceAll("in .+!$", "in %2\\$s!"),
                text -> {
                    Matcher m = Pattern.compile("in (.+)!$").matcher(text.getString());
                    boolean ignore = m.find();
                    return ITime.translateTime(m.group(1));
                }),
        BANK_PAGE_ADDED(Pattern.compile("You have unlocked page"),
                text -> text.getString().replaceAll("\\d+", "%2\\$s"),
                text -> text.getString().replaceAll("\\D", ""));

        private final Pattern template;
        private final Function<Text, String> mutator;
        private final Function<Text, Object> argumentParser;

        Templates(Pattern template, Function<Text, String> mutator, Function<Text, Object> argumentParser) {
            this.template = template;
            this.mutator = mutator;
            this.argumentParser = argumentParser;
        }

        private static Templates findTemplate(Text text) {
            return Arrays.stream(Templates.values())
                    .filter(templates -> templates.template.matcher(text.getString()).find())
                    .findFirst().orElse(null);
        }
    }
}
