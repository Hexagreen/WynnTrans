package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;

public class BossAltar extends WynnDisplayText {
    private final Text difficulty;
    private final Text altarName;
    private final Text tribute;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 3) return false;
        return text.getString().contains("\uE001\uE00E\uE012\uE012 \uE000\uE00B\uE013\uE000\uE011\uDB00\uDC02");
    }

    public BossAltar(Text text) {
        super(text);
        this.difficulty = getDifficulty(getSibling(2));
        this.altarName = getAltarName(getSibling(2));
        this.tribute = getTribute(getSibling(2));
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(getSibling(0)).append("\n")
                .append(difficulty).append("\n\n")
                .append(altarName).append("\n\n")
                .append(tribute);
    }

    private Text getDifficulty(Text text) {
        Text diff = text.getSiblings().getFirst();
        Style style = diff.getStyle();
        String strDiff = diff.copyContentOnly().getString().replaceFirst("\n(.|\n)+$", "")
                .replaceAll(" ", "").toLowerCase(Locale.ENGLISH);
        Text difficulty = Text.translatable("wytr.difficulty." + strDiff).setStyle(style);
        return Text.translatable("wytr.difficulty", difficulty).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    }

    private Text getAltarName(Text text) {
        Text name = text.getSiblings().getFirst().getSiblings().getFirst();
        Style style = name.getStyle();
        String valName = name.getString();
        String keyName = "wytr.bossAltar." + normalizeStringForKey(valName);
        if(WTS.checkTranslationExist(keyName, valName)) {
            return Text.translatable(keyName).setStyle(style);
        }
        else {
            return Text.literal(valName).setStyle(style);
        }
    }

    private Text getTribute(Text text) {
        return text.getSiblings().getFirst().getSiblings().getLast();
    }
}
