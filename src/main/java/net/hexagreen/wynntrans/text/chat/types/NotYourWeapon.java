package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CharacterClass;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NotYourWeapon extends WynnChatText {
    private final Text item;
    private final Text className;
    private final Text weapon;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("§c(.+)§4 is not a §c(.+)§4 weapon\\. You must use a §c.+\\.").matcher(text.getString()).find();
    }

    public NotYourWeapon(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("§c(.+)§4 is not a §c(.+)§4 weapon\\. You must use a §c.+\\.").matcher(inputText.getString());
        boolean ignore = matcher.find();
        this.item = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.RED));
        this.className = CharacterClass.getClassName(matcher.group(2)).setStyle(Style.EMPTY.withColor(Formatting.RED));
        this.weapon = CharacterClass.getWeapon(matcher.group(2)).setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.illegalWeapon";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, item, className, weapon).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
