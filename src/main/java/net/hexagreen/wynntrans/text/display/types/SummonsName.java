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
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("^§b.+'s§7 (?:Puppet|Effigy|Hound)\\n§7\\d+[ms]$");
    }

    public SummonsName(Text text) {
        super(text);
        String str = getContentString();
        this.owner = str.replaceFirst("'s§7 .+\\n.+", "");
        this.summons = Text.literal(str.replaceAll(".+'s§7 |\\n§7.+", "")).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        this.timer = ITime.translateTime(str.replaceFirst(".+\\n§7", "")).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.summons";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = newTranslate(parentKey, owner, summons, timer).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
    }
}
