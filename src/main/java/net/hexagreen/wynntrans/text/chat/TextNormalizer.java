package net.hexagreen.wynntrans.text.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    private void argsToTranslatable(String baseKey, BiFunction<String, String, Boolean> translationRegister) {
        for(int i = 0; i < args.size(); i++) {
            if(flags.get(i)) continue;

            Text current = args.get(i);
            String value = current.getString();
            Style style = current.getStyle().withParent(Style.EMPTY.withBold(false).withItalic(false).withUnderline(false).withObfuscated(false).withStrikethrough(false));
            Text mutated;
            if(value.contains("%p") || value.contains("%s")) {
                int argsCount = value.split("%s", -1).length - 1;
                boolean containsP = value.contains("%p");
                if(containsP) {
                    argsCount++;
                    value = value.replaceAll("%p", "%1\\$s");
                }
                List<Text> selectedArgs = new ArrayList<>();
                for(int j = argsCount; j > 0; j--) {
                    selectedArgs.add(args.remove(i - argsCount));
                    flags.remove(i - argsCount);
                }
                i -= argsCount;
                String saltedKey = baseKey + "." + (i + 1) + "_" + DigestUtils.sha1Hex(value).substring(0, 4);
                if(translationRegister.apply(saltedKey, value)) {
                    mutated = Text.translatable(saltedKey, selectedArgs.toArray(new Object[0])).setStyle(style);
                }
                else {
                    MutableText reassembled = Text.empty().setStyle(style);
                    String[] strings = value.split("%1\\$s", -1);
                    Text playerName = null;
                    if(containsP) playerName = selectedArgs.removeFirst();
                    for(int j = 0, l = strings.length; j < l; j++) {
                        if(!strings[j].isEmpty()) {
                            String[] subOfStrings = strings[j].split("%s", -1);
                            for(int k = 0, m = subOfStrings.length; k < m; k++) {
                                if(!subOfStrings[k].isEmpty()) reassembled.append(subOfStrings[k]);
                                if(k != m - 1) reassembled.append(selectedArgs.removeFirst());
                            }
                        }
                        if(j != l - 1 && playerName != null) reassembled.append(playerName);
                    }
                    mutated = reassembled;
                }
            }
            else {
                String saltedKey = baseKey + "." + (i + 1) + "_" + DigestUtils.sha1Hex(value).substring(0, 4);
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
                    flags.add(true);
                }
                string = string.replaceFirst(rule.pattern(), rule.replace());
            }

            if(Objects.equals(desiredStyle.getColor(), sibling.getStyle().getColor())) {
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
