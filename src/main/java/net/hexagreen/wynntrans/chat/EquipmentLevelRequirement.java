package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class EquipmentLevelRequirement extends WynnChatText {
    public EquipmentLevelRequirement(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "equipLevelReq";
    }

    @Override
    protected void build() {
        Text equipName = getSibling(0);
        Text level = getSibling(2);

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, equipName, level).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
