package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemLevelRequirement extends WynnSystemText {
    private final Text level;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("This (?:item|potion) is for Combat Lv\\. (\\d+)\\+ only\\.").matcher(text.getString()).find();
    }

    public ItemLevelRequirement(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("This (?:item|potion) is for Combat Lv\\. (\\d+)\\+ only\\.").matcher(inputText.getString().replaceAll("\n", ""));
        boolean ignore = matcher.find();
        this.level = Text.literal(matcher.group(1));
    }

    @Override
    protected String setTranslationKey() {
        if(inputText.getString().contains("potion")) return rootKey + "func.potionLevelReq";
        return rootKey + "func.itemLevelReq";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
        resultText.append(Text.translatable(translationKey, level));
    }
}
