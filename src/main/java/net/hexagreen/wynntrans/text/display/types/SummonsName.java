package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.List;

public class SummonsName extends WynnDisplayText {

    public static boolean typeChecker(Text text) {
        return text.getString().matches(".+'s (?:Puppet|Effigy|Bird)(?:\\n. \\d+[ms])?");
    }

    public SummonsName(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.summons";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text owner = Text.literal(getContentString().replaceFirst("'s ", "")).setStyle(getStyle());
        Text summons = Text.literal(getContentString(0).replaceFirst("\\n", "")).setStyle(GRAY);

        List<Text> nested = getSibling(0).getSiblings();
        if(!nested.isEmpty()) {
            Text timer = nested.getFirst().copy().setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)).append(" ")
                    .append(ITime.translateTime(nested.getLast().getString()).setStyle(GRAY));
            resultText = Text.translatable(translationKey + ".duration", owner, summons, timer);
            return;
        }

        resultText = Text.translatable(translationKey, owner, summons).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
    }
}
