package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Identifier extends WynnChatText {
    public Identifier(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "identifier";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)))
                .append(Text.literal(": ").setStyle(getStyle(0)))
                .append(newTranslate(parentKey + ".1").setStyle(getStyle(1)))
                .append(ItemRarity.UNIQUE.getRarity())
                .append(newTranslate(parentKey + ".separator").setStyle(getStyle(1)))
                .append(ItemRarity.RARE.getRarity())
                .append(newTranslate(parentKey + ".separator").setStyle(getStyle(1)))
                .append(ItemRarity.LEGENDARY.getRarity())
                .append(newTranslate(parentKey + ".separator").setStyle(getStyle(1)))
                .append(ItemRarity.SET.getRarity())
                .append(newTranslate(parentKey + ".separator").setStyle(getStyle(1)))
                .append(ItemRarity.FABLED.getRarity())
                .append(newTranslate(parentKey + ".separator_alt").setStyle(getStyle(1)))
                .append(ItemRarity.MYTHIC.getRarity())
                .append(newTranslate(parentKey + ".2").setStyle(getStyle(1)));
    }
}
