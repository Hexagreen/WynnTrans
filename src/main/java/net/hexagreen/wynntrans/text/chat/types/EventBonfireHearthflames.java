package net.hexagreen.wynntrans.text.chat.types;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class EventBonfireHearthflames extends SimpleSystemText {
    private final String num;

    public static boolean typeChecker(Text text) {
        String input = removeTextBox(text);
        return Pattern.compile("You are carrying \\d+ hearthflames").matcher(input).find()
                || Pattern.compile("Returning \\d+ hearthflames").matcher(input).find()
                || Pattern.compile("Returned \\d+ hearthflames").matcher(input).find();
    }

    private static Text preprocess(Text text) {
        MutableText result = numToReplacer(text);
        for(Text sibling : text.getSiblings()) {
            result.append(preprocess(sibling));
        }
        return result;
    }

    private static MutableText numToReplacer(Text text) {
        String content = text.copyContentOnly().getString();
        if(content.matches(".*\\d+.*")) {
            return Text.literal(content.replaceAll("\\d+", "%2\\$s")).setStyle(text.getStyle());
        }
        else return text.copyContentOnly().setStyle(text.getStyle());
    }

    public EventBonfireHearthflames(Text text) {
        super(preprocess(text));
        this.num = text.getString().replaceAll("\\D", "");
    }

    @Override
    protected MutableText newTranslateWithSplit(String key) {
        return Text.translatable(key, splitter, num);
    }
}
