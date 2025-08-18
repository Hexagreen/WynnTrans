package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CharacterClass;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class NotYourWeapon extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("(.+) is not an? (.+) weapon\\. You must use an? .+").matcher(removeTextBox(text)).find();
    }

    public NotYourWeapon(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.illegalWeapon";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text item = new ItemName(getSibling(0)).textAsMutable();
        String _className = getContentString(2).replaceFirst("an? ", "");
        Text className = CharacterClass.getClassName(_className).formatted(Formatting.RED);
        Text weapon = CharacterClass.getWeapon(_className).formatted(Formatting.RED);

        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, item, className, weapon).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
