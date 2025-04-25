package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class EquipmentLevelRequirement extends WynnSystemText {
    private final Text equipName;
    private final Text level;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("You must be at least combat level \\d+ to use ").matcher(text.getString()).find();
    }

    public EquipmentLevelRequirement(Text text) {
        super(text);
        this.equipName = new ItemName(getSibling(-1)).textAsMutable();
        this.level = getSibling(1);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.equipLevelReq";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, equipName, level).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
