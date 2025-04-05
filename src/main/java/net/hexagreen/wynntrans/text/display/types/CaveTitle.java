package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class CaveTitle extends WynnDisplayText {
    private final String keyCaveName;
    private final String valCaveName;
    private final Style styleCaveName;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 3) return false;
        return text.getString().contains("\uE002\uE000\uE015\uE004") || text.getString().contains("\uE013\uE00E\uE016\uE004\uE011") || text.getString().contains("\uE002\uE000\uE00C\uE00F");
    }

    public CaveTitle(Text text) {
        super(text);
        this.valCaveName = getSibling(2).getString();
        this.keyCaveName = "wytr.cave." + normalizeStringForKey(valCaveName);
        this.styleCaveName = getStyle(2);
    }

    @Override
    protected String setTranslationKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text cave;
        if(WTS.checkTranslationExist(keyCaveName, valCaveName)) {
            cave = Text.translatable(keyCaveName).setStyle(styleCaveName);
        }
        else {
            cave = Text.literal(valCaveName).setStyle(styleCaveName);
        }

        resultText = Text.empty().setStyle(getStyle());
        resultText.append(getSibling(0)).append("\n").append(cave);
    }
}
