package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class EquipmentSkillRequirement extends WynnChatText {
    private final Text equipName;
    private final Text skill;
    private final Text level;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§c(.+)§4 requires your (.+) skill to be at least §c(\\d+)\\.").matcher(text.getString()).find();
    }

    public EquipmentSkillRequirement(Text text) {
        super(text, Pattern.compile("^§c(.+)§4 requires your (.+) skill to be at least §c(\\d+)\\."));
        this.equipName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.RED));
        this.skill = CharacterSkill.getSkill(matcher.group(2));
        this.level = Text.literal(matcher.group(3)).setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.equipSkillReq";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, equipName, skill, level).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
