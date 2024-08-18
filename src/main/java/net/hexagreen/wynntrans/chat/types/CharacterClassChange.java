package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.enums.CharacterClass;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class CharacterClassChange extends WynnChatText {
    private final Text className;

    public CharacterClassChange(Text text, Pattern regex) {
        super(text, regex);
        this.className = CharacterClass.getClassName(getContentString().replaceAll(".+§c", ""))
                .setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.classChange";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, className).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
