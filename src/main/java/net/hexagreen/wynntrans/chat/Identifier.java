package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.enums.ItemRarity;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Identifier extends WynnChatText {
    protected Identifier(Text text, Pattern regex) {
        super(text, regex);
    }

    public static Identifier of(Text text, Pattern regex) {
        return new Identifier(text, regex);
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
                .append(newTranslate(parentKey + "_1").setStyle(getStyle(1)))
                .append(ItemRarity.UNIQUE.getRarity())
                .append(newTranslate(parentKey + "_separator").setStyle(getStyle(1)))
                .append(ItemRarity.RARE.getRarity())
                .append(newTranslate(parentKey + "_separator").setStyle(getStyle(1)))
                .append(ItemRarity.LEGENDARY.getRarity())
                .append(newTranslate(parentKey + "_separator").setStyle(getStyle(1)))
                .append(ItemRarity.SET.getRarity())
                .append(newTranslate(parentKey + "_separator").setStyle(getStyle(1)))
                .append(ItemRarity.FABLED.getRarity())
                .append(newTranslate(parentKey + "_separator_alt").setStyle(getStyle(1)))
                .append(ItemRarity.MYTHIC.getRarity())
                .append(newTranslate(parentKey + "_2").setStyle(getStyle(1)));
    }
}
