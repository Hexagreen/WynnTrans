package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BoostTotemRunning extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("You are inside of (.+)'s (mob|gathering) totem").matcher(text.getString()).find();
    }

    public BoostTotemRunning(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return getContentString(2).contains("mob") ? rootKey + "func.mobTotemRunning" : rootKey + "func.gatheringTotemRunning";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text playerName = getPlayerName();
        Text link = Text.literal(getContentString(-1).replaceAll("\\n", "").trim())
                .setStyle(getStyle(-1));

        resultText = Text.empty().setStyle(getStyle());
        resultText.append(Text.translatable(translationKey, playerName, link));
    }

    private Text getPlayerName() {
        String name = getContentString(1).replaceFirst("'s?", "");
        return Text.literal(name).setStyle(getStyle(1));
    }
}
