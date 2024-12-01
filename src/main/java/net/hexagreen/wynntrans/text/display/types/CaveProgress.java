package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CaveProgress extends WynnDisplayText {
    private final boolean completed;
    private final String bar;

    public static boolean typeChecker(Text text) {
        return text.getString().matches("^§7Progress\\n.+");
    }

    public CaveProgress(Text text) {
        super(text);
        this.completed = text.getString().contains("Completed");
        this.bar = text.getString().split("\\n")[1];
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.caveProgress";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n");
        if(completed)
            resultText.append(Text.translatable("wytr.display.caveComplete").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        else resultText.append(bar);
    }
}
