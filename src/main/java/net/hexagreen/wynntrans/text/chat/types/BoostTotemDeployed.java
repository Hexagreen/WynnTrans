package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BoostTotemDeployed extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile(" has placed a (mob|gathering) totem in ").matcher(text.getString()).find();
    }

    public BoostTotemDeployed(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return getContentString(1).contains("mob") ? rootKey + "func.mobTotemDeployed" : rootKey + "func.gatheringTotemDeployed";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text playerName = getSibling(0);
        Text location = getLocation(getSibling(2));
        Text coordinate = Text.literal(getContentString(4).replaceAll("\\n", "").trim())
                .setStyle(getStyle(4));
        Text store = Text.literal(getContentString(-1).replaceAll("\\n", "").trim())
                .setStyle(getStyle(-1));

        resultText = Text.empty().setStyle(getStyle());
        resultText.append(Text.translatable(translationKey, playerName, location, coordinate, store));
    }

    private Text getLocation(Text text) {
        String areaName = normalizeStringForKey(text.getString());
        return Text.translatable(rootKey + "area." + areaName);
    }
}
