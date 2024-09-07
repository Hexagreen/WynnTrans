package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class BossAltar extends WynnDisplayText {
    private final Text altarName;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 5) return false;
        return text.getString().contains("\uE001\uE00E\uE012\uE012 \uE000\uE00B\uE013\uE000\uE011\uDB00\uDC02");
    }

    public BossAltar(Text text) {
        super(text);
        this.altarName = getAltarName(getSibling(2));
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(getSibling(0)).append("\n")
                .append(altarName).append("\n\n")
                .append(getSibling(4));
    }

    private Text getAltarName(Text text) {
        Style style = text.getStyle();
        String valName = text.getString();
        String keyName = "wytr.bossAltar." + normalizeStringForKey(valName);
        if(WTS.checkTranslationExist(keyName, valName)) {
            return newTranslate(keyName).setStyle(style);
        }
        else {
            return Text.literal(valName).setStyle(style);
        }
    }
}
