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

    public static boolean typeChecker(Text text) {
        return Templates.findTemplate(text) != null;
    }

    public ProxySystemText(Text text) {
        super(text);
        this.template = Templates.findTemplate(inputText);
    }

    @Override
    protected String initValText() {
        return Templates.findTemplate(inputText).mutator.apply(inputText);
    }

    @Override
    protected MutableText newTranslate(String key) {
        return Text.translatable(key, template.argumentParser.apply(inputText));
    }

    private enum Templates {
        ANNIHILATION(Pattern.compile("Prepare to defend the province at"),
                text -> text.getString().replaceAll("in .+!$", "in %1\\$s!"),
                text -> {
                    Matcher m = Pattern.compile("in (.+)!$").matcher(text.getString());
                    boolean ignore = m.find();
                    return ITime.translateTime(m.group(1));
                }),
        BANK_PAGE_ADDED(Pattern.compile("You have unlocked page"),
                text -> text.getString().replaceAll("\\d+", "%1\\$s"),
                text -> text.getString().replaceAll("\\D", ""));

        private final Pattern template;
        private final Function<Text, String> mutator;
        private final Function<Text, Object> argumentParser;

        private static Templates findTemplate(Text text) {
            return Arrays.stream(Templates.values())
                    .filter(templates -> templates.template.matcher(text.getString()).find())
                    .findFirst()
                    .orElse(null);
        }

        Templates(Pattern template, Function<Text, String> mutator, Function<Text, Object> argumentParser) {
            this.template = template;
            this.mutator = mutator;
            this.argumentParser = argumentParser;
        }
    }
}
