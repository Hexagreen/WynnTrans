package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class TrinketCooldown extends WynnSystemText {
    private final Text item;
    private final Text time;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("is on cooldown for ").matcher(removeTextBox(text)).find();
    }

    public TrinketCooldown(Text text) {
        super(text);
        this.item = getSibling(0);
        this.time = ITime.translateTime(getContentString(2)).setStyle(getStyle(2));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.trinketCooldown";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, item, time).setStyle(GRAY));
    }
}
