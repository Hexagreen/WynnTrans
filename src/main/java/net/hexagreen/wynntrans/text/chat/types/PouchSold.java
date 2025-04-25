package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class PouchSold extends WynnChatText {
    private final String amount;
    private final String emerald;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§dYou have sold (§7\\d+)§d ingredients for a total of (§a.+)§d").matcher(text.getString()).find();
    }

    public PouchSold(Text text) {
        super(text);
        this.amount = inputText.getString().replaceFirst(".+(§7\\d+).+", "$1");
        this.emerald = inputText.getString().replaceFirst(".+total of (§a.+)§d.+", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.pouchSold";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, amount, emerald).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }
}
