package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.enums.CharacterClass;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CharacterClassChange extends WynnChatText {
    private final Text className;

    public CharacterClassChange(Text text, Pattern regex) {
        super(text, regex);
        this.className = CharacterClass.getClassName(getSibling(1).getString()).setStyle(getStyle(1));
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "classChange";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, className).setStyle(getStyle(0)));
    }
}
