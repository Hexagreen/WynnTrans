package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BoostTotemRunOut extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("(.+)'s (Mob|Gathering) Totem has run out").matcher(text.getString()).find();
    }

    public BoostTotemRunOut(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return getContentString(1).contains("Mob") ? rootKey + "func.mobTotemRunOut" : rootKey + "func.gatheringTotemRunOut";
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
        String name = getContentString(0).replaceFirst("'s?", "");
        return Text.literal(name).setStyle(getStyle(0));
    }
}
