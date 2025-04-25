package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class BossAltar extends WynnDisplayText {
    private final Text recommendLevel;
    private final Text altarName;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 5) return false;
        return text.getString().contains("\uE001\uE00E\uE012\uE012 \uE000\uE00B\uE013\uE000\uE011\uDB00\uDC02");
    }

    public BossAltar(Text text) {
        super(text);
        this.recommendLevel = getRecommendLevel(getSibling(4));
        this.altarName = getAltarName(getSibling(2));
    }

    @Override
    protected String setTranslationKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(getSibling(0)).append("\n")
                .append(altarName).append("\n\n")
                .append(recommendLevel);
    }

    private Text getRecommendLevel(Text text) {
        return Text.translatable("wytr.requirement.combatRecommend", text.getSiblings().getFirst()).setStyle(GRAY);
    }

    private Text getAltarName(Text text) {
        Style style = text.getStyle();
        String valName = text.getString();
        String keyName = "wytr.bossAltar." + normalizeStringForKey(valName);
        if(WTS.checkTranslationExist(keyName, valName)) {
            return Text.translatable(keyName).setStyle(style);
        }
        else {
            return Text.literal(valName).setStyle(style);
        }
    }
}
