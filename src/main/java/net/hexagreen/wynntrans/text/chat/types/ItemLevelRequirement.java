package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ItemLevelRequirement extends WynnSystemText {
    private final Text level;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("This (?:item|potion) is for Combat Lv\\. (\\d+)\\+ only\\.").matcher(text.getString()).find();
    }

    public ItemLevelRequirement(Text text) {
        super(text, Pattern.compile("This (?:item|potion) is for Combat Lv\\. (\\d+)\\+ only\\."));
        this.level = Text.literal(matcher.group(1));
    }

    @Override
    protected String setParentKey() {
        if(inputText.getString().contains("potion")) return rootKey + "func.potionLevelReq";
        return rootKey + "func.itemLevelReq";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().append(header).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
        resultText.append(newTranslateWithSplit(parentKey, level));
    }
}
