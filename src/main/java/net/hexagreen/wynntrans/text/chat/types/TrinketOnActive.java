package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class TrinketOnActive extends WynnSystemText {
    private final Text item;
    private final Text time;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("Your .+ is active for |is currently active for").matcher(removeTextBox(text)).find();
    }

    public TrinketOnActive(Text text) {
        super(text);
        boolean currently = inputText.getString().contains("is currently active");
        this.item = currently ? getSibling(0) : getSibling(1);
        this.time = ITime.translateTime(getContentString(-2)).setStyle(getStyle(-2));
    }

    @Override
    protected String setTranslationKey() {
        return inputText.getString().contains("is currently active") ? rootKey + "func.trinketOnActiveCurr" : rootKey + "func.trinketOnActive";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(Text.translatable(translationKey, item, time));
    }
}
