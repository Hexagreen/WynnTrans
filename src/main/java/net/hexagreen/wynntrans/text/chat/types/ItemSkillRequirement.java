package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemSkillRequirement extends WynnSystemText {
    private final Text skill;
    private final Text level;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("This item requires your (.+) skill to be at least §c(\\d+)\\.").matcher(removeTextBox(text)).find();
    }

    public ItemSkillRequirement(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("This item requires your (.+) skill to be at least §c(\\d+)\\.").matcher(inputText.getString().replaceAll("\n", ""));
        boolean ignore = matcher.find();
        this.skill = CharacterSkill.getSkill(matcher.group(1));
        this.level = Text.literal(matcher.group(2)).setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.itemSkillReq";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
        resultText.append(Text.translatable(translationKey, skill, level));
    }
}
