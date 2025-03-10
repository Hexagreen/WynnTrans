package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SummonsName extends WynnDisplayText {
    private final String owner;
    private final Text summons;
    private final Text timer;

    public static boolean typeChecker(Text text) {
        return text.getString().matches(".+'s§7 (?:Puppet|Effigy|Hound|Crow)\\n§7\\d+[ms]");
    }

    public SummonsName(Text text) {
        super(text);
        String str = inputText.getString();
        this.owner = str.replaceFirst("'s§7 .+\\n.+", "");
        this.summons = Text.literal(str.replaceAll(".+'s§7 |\\n.+", "")).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        this.timer = ITime.translateTime(str.replaceFirst(".+\\n", "")).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.summons";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.translatable(translationKey, owner, summons, timer).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
    }
}
