package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.MutableText;
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
    public final Text originalText;
    private final Rulebooks.NormalizerRulebook rulebook;
    protected List<Text> copiedSiblings;
    /**
     * Normalized text. May contain string replacer for arguments
     */
    protected Text text;
    /**
     * Arguments that insert to text. Arguments can be translated
     */
    protected List<Text> args;
    /**
     * Flags for bypass argument translation
     */
    protected List<Boolean> flags;

    protected TextNormalizer(Text text, Rulebooks.NormalizerRulebook rulebook) {
        this.originalText = text;
        this.copiedSiblings = text.getSiblings() == null ? new ArrayList<>() : new ArrayList<>(text.getSiblings());
        this.rulebook = rulebook;
        normalize(text);
    }

    protected abstract void normalize(Text text);

    protected boolean argsFilter(Text sibling) {
        return false;
    }

    public Text getText() {
        return text;
    }

    public List<Text> getArgs(String baseKey, BiFunction<String, String, Boolean> translationRegister) {
        argsToTranslatable(baseKey, translationRegister);
        return args;
    }

    private void argsToTranslatable(String baseKey, BiFunction<String, String, Boolean> translationRegister) {
        List<Text> _nestedArgs = new ArrayList<>();
        List<Boolean> _nestedFlags = new ArrayList<>();
        for(int i = args.size() - 1; i >= 0; i--) {
            Text current = args.get(i);
            String value = current.getString();
            int nestedCount = value.split("%s", -1).length - 1;
            if(value.contains("%p")) nestedCount++;
            for(int j = 1; j <= nestedCount; j++) {
                _nestedArgs.add(args.remove(i - j));
                _nestedFlags.add(flags.remove(i - j));
            }
            i -= nestedCount;
        }
        List<Text> nestedArgs = _nestedArgs.reversed();
        List<Boolean> nestedFlags = _nestedFlags.reversed();

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
                    value = value.replaceAll("%p", "%1\\$s");
                    argsCount++;
                }

                List<Text> selectedArgs = new ArrayList<>();
                String saltedKey = baseKey + "." + (i + 1) + "_" + DigestUtils.sha1Hex(value).substring(0, 4);
                for(int j = 1; j <= argsCount; j++) {
                    Text arg = nestedArgs.removeFirst();
                    boolean flag = nestedFlags.removeFirst();
                    String saltyVal = arg.getString();
                    String saltyKey = saltedKey + "." + j + "_" + DigestUtils.sha1Hex(saltyVal).substring(0, 4);
                    Text translatedArg;
                    if(flag && translationRegister.apply(saltyKey, saltyVal)) {
                        translatedArg = Text.translatable(saltyKey).setStyle(arg.getStyle());
                    }
                    else {
                        translatedArg = arg;
                    }
                    selectedArgs.add(translatedArg);
                }

                if(translationRegister.apply(saltedKey, value)) {
                    mutated = Text.translatable(saltedKey, selectedArgs.toArray(Object[]::new)).setStyle(style);
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
                Matcher itemNameMatcher = Pattern.compile(" ?\\[(\\d+) (.+)] ?").matcher(value);
                if(itemNameMatcher.find()) {
                    String quantity = itemNameMatcher.group(1);
                    String itemName = itemNameMatcher.group(2);
                    Text itemNameText = new ItemName(itemName).textAsMutable();
                    mutated = Text.translatable("wytr.func.questingItem", quantity, itemNameText.getString()).setStyle(style);
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
            }
            args.set(i, mutated);
        }
    }

    protected ArgsRecord siblingsToArgs(List<Text> textBody, Style desiredStyle) {
        String wholeText = originalText.getString();
        List<Text> args = new ArrayList<>();
        List<Boolean> flags = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        String playerName = WynnTrans.wynnPlayerName;
        int[] ruleNumber = {0};

        textBody.forEach(sibling -> {
            if(sibling.getString().isEmpty()) return;

            if(sibling.getString().matches("[\\n ]+")) {
                currentString.append(sibling.getString());
                return;
            }

            if(argsFilter(sibling)) {
                args.add(sibling);
                flags.add(true);
                currentString.append("%s");
                return;
            }

            String string = sibling.getString();
            if(string.contains(playerName)) {
                args.add(Text.literal(playerName).setStyle(sibling.getStyle()));
                flags.add(true);
                string = string.replaceAll(playerName, "%p");
            }

            Rulebooks.NormalizerRule rule = rulebook.findRule(wholeText, ruleNumber[0]);
            if(rule != null) {
                Matcher matcher = Pattern.compile(rule.pattern()).matcher(string);
                if(matcher.find()) {
                    ruleNumber[0]++;
                    int groups = matcher.groupCount();
                    for(int i = 1; i <= groups; i++) {
                        args.add(Text.literal(matcher.group(i)).setStyle(sibling.getStyle()));
                        flags.add(!rule.registration());
                    }
                    string = string.replaceFirst(rule.pattern(), rule.replace());
                }
            }

            if(Objects.equals(desiredStyle, sibling.getStyle())) {
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

    protected record ArgsRecord(String textContent, List<Text> args, List<Boolean> flags) {
    }
}
