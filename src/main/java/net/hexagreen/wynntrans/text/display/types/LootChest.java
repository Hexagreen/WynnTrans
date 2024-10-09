package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

public class LootChest extends WynnDisplayText {
    private final Style color;
    private final String star;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches("^§.Loot Chest §..+");
    }

    public LootChest(Text text) {
        super(text);
        this.color = parseStyleCode(text.getString().substring(0, 2));
        this.star = getContentString().replaceFirst("^§.Loot Chest ", "");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.lootChest";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, star).setStyle(color));
    }
}
