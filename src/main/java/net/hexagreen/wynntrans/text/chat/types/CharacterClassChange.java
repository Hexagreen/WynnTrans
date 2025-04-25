package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CharacterClass;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class CharacterClassChange extends WynnChatText {
    private final Text className;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§4Your character's class has been successfully changed to ").matcher(text.getString()).find();
    }

    public CharacterClassChange(Text text) {
        super(text);
        this.className = CharacterClass.getClassName(getContentString().replaceAll(".+§c", "")).setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.classChange";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, className).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
