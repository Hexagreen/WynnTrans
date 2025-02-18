package net.hexagreen.wynntrans.text.title.types;

import net.hexagreen.wynntrans.text.title.WynnTitleText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IngredientPouch extends WynnTitleText {
    private final String count;
    private final Text ingredient;

    public static boolean typeChecker(Text text) {
        return text.getString().matches("§a\\+\\d+ §7.+§a to pouch");
    }

    public IngredientPouch(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("§a\\+(\\d+) §7(.+)§a to pouch").matcher(getContentString());
        boolean ignore = matcher.find();
        this.count = matcher.group(1);
        this.ingredient = new ItemName(matcher.group(2)).textAsMutable().setStyle(Style.EMPTY.withColor(Formatting.GRAY));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "title.ingredientPouch";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.translatable(translationKey, count, ingredient).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
    }
}
