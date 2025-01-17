package net.hexagreen.wynntrans.text.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TextNormalizer {
    private final Rulebooks.NormalizerRulebook rulebook;
    protected Text text;
    protected List<Text> copiedSiblings;
    protected List<Text> args;
    protected List<Boolean> flags;

    protected TextNormalizer(Text text, Rulebooks.NormalizerRulebook rulebook) {
        this.copiedSiblings = text.getSiblings() == null ? new ArrayList<>() : new ArrayList<>(text.getSiblings());
        this.rulebook = rulebook;
        normalizer(text);
    }

    protected abstract void normalizer(Text text);

    public Text getText() {
        return text;
    }

    public List<Text> getArgs(String baseKey, BiFunction<String, String, Boolean> translationRegister) {
        argsToTranslatable(baseKey, translationRegister);
        return args;
    }

    protected String argsTransKeyMutator(String baseKey, String value) {
        return baseKey;
    }

    private void argsToTranslatable(String baseKey, BiFunction<String, String, Boolean> translationRegister) {
        for(int i = 0; i < args.size(); i++) {
            if(flags.get(i)) continue;

            String key = baseKey + "." + (i + 1);
            Text current = args.get(i);
            String value = current.getString();
            Style style = current.getStyle().withParent(Style.EMPTY.withBold(false).withItalic(false).withUnderline(false).withObfuscated(false).withStrikethrough(false));
            Text mutated;
            if(value.contains("%p")) {
                value = value.replaceAll("%p", "%1\\$s");
                Text playerName = args.get(i - 1);
                String saltedKey = argsTransKeyMutator(key, value);
                if(translationRegister.apply(saltedKey, value)) {
                    mutated = Text.translatable(saltedKey, playerName).setStyle(style);
                }
                else {
                    MutableText reassembled = Text.empty().setStyle(style);
                    String[] strings = (value + "%1$sEOL").split("%1\\$s");
                    for(int j = 0, l = strings.length - 1; j < l; j++) {
                        if(!strings[j].isEmpty()) reassembled.append(strings[j]);
                        if(j != l - 1) reassembled.append(playerName);
                    }
                    mutated = reassembled;
                }
            }
            else {
                String saltedKey = argsTransKeyMutator(key, value);
                if(translationRegister.apply(saltedKey, value)) {
                    mutated = Text.translatable(saltedKey).setStyle(style);
                }
                else {
                    mutated = current.copy().setStyle(style);
                }
            }
            args.set(i, mutated);
        }
    }

    protected ArgsRecord siblingsToArgs(List<Text> textBody, Style desiredStyle) {
        List<Text> args = new ArrayList<>();
        List<Boolean> flags = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        int[] bodySize = {textBody.size()};
        @SuppressWarnings("DataFlowIssue") String clientPlayerName = MinecraftClient.getInstance().player.getName().getString();

        textBody.forEach(sibling -> {
            if(sibling.getContent().equals(PlainTextContent.EMPTY)) {
                args.add(sibling.copy());
                flags.add(true);
                currentString.append("%p");
                bodySize[0]--;
                return;
            }
            String string = sibling.getString();
            if(string.contains(clientPlayerName)) {
                args.add(Text.literal(clientPlayerName).setStyle(sibling.getStyle()));
                flags.add(true);
                string = string.replaceAll(clientPlayerName, "%p");
            }

            Rulebooks.NormalizerRule rule = rulebook.findRule(string);
            if(rule != null) {
                Matcher matcher = Pattern.compile(rule.pattern()).matcher(string);
                boolean ignore = matcher.find();
                int groups = matcher.groupCount();
                for(int i = 1; i <= groups; i++) {
                    args.add(Text.literal(matcher.group(i)).setStyle(sibling.getStyle()));
                    flags.add(false);
                }
                string = string.replaceFirst(rule.pattern(), rule.replace());
            }

            if(desiredStyle.equals(sibling.getStyle())) {
                currentString.append(string);
            }
            else {
                args.add(Text.literal(string).setStyle(sibling.getStyle()));
                flags.add(false);
                currentString.append("%s");
            }
        });
        String textContent = currentString.toString().replaceAll("%p", "%s");
        return new ArgsRecord(textContent, args, flags);
    }

    protected boolean hasNotMatchWithRule(String target) {
        return rulebook.findRule(target) == null;
    }

    protected record ArgsRecord(String textContent, List<Text> args, List<Boolean> flags) {
    }
}
