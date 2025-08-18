package net.hexagreen.wynntrans.text.title.types;

import net.hexagreen.wynntrans.text.title.WynnTitleText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class DiscoveryFound extends WynnTitleText {

    public static boolean typeChecker(Text text) {
        String string = text.getString().replaceAll("§.", "");
        return WTS.checkTranslationDoNotRegister("wytr.discovery." + normalizeStringForKey(string));
    }

    public DiscoveryFound(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Style style = parseStyleCode(getContentString());
        resultText = Text.translatable("wytr.discovery." + normalizeStringForKey(getContentString().replaceAll("§.", ""))).setStyle(style);
    }
}
