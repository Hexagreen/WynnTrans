package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ManaPotion extends WynnChatText {
    private static final Pattern REGEX = Pattern.compile("§b✺ (\\+\\d) mana/s for (\\d seconds)]");

    public static boolean typeChecker(Text text) {
        return REGEX.matcher(text.getString()).find();
    }

    public ManaPotion(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.manaPotion";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Matcher matcher = REGEX.matcher(getContentString());
        if(matcher.find()) {
            String amount = matcher.group(1);
            Text duration = ITime.translateTime(matcher.group(2));
            resultText = Text.translatable(translationKey, amount, duration);
        }
        else {
            resultText = inputText;
        }
    }
}
