package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class EquipmentLevelRequirement extends WynnChatText {
    private final Text equipName;
    private final Text level;

    public EquipmentLevelRequirement(Text text, Pattern regex) {
        super(text, regex);
        this.equipName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.RED));
        this.level = Text.literal(matcher.group(2)).setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.equipLevelReq";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, equipName, level).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
