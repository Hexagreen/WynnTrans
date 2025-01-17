package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;
import java.util.regex.Pattern;

public class Disguising extends WynnChatText {
    private final boolean apply;
    private final Text target;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§3You are (now|no longer) disguised as an? §b(.+)").matcher(text.getString()).find();
    }

    public Disguising(Text text) {
        super(text);
        this.apply = inputText.getString().contains("You are now");
        this.target = Text.literal(capitalizeFirstChar(inputText.getString().replaceFirst(".+disguised as an? §b(.+)", "$1"))).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.disguise.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA));
        String key = apply ? translationKey + "on" : translationKey + "off";
        resultText.append(Text.translatable(key, target));
    }

    private String capitalizeFirstChar(String input) {
        String body = input.substring(1);
        char head = input.toUpperCase(Locale.ENGLISH).charAt(0);

        return head + body;
    }
}
