package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.Locale;
import java.util.regex.Pattern;

public class Disguising extends WynnChatText {
    private final boolean apply;
    private final Text target;

    public Disguising(Text text, Pattern regex) {
        super(text, regex);
        this.apply = matcher.group(1).equals("now");
        this.target = Text.literal(capitalizeFirstChar(getSibling(1).getString())).setStyle(getStyle(1));
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "disguise.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        String key = apply ? parentKey + "on" : parentKey + "off";
        resultText.append(newTranslate(key, target).setStyle(getStyle(0)));
    }

    private String capitalizeFirstChar(String input) {
        String body = input.substring(1);
        char head = input.toUpperCase(Locale.ENGLISH).charAt(0);

        return head + body;
    }
}
