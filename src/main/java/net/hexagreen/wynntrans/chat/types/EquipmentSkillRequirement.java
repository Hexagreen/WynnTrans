package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class EquipmentSkillRequirement extends WynnChatText {
    private final Text equipName;
    private final Text skill;
    private final Text level;

    public EquipmentSkillRequirement(Text text, Pattern regex) {
        super(text, regex);
        this.equipName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.RED));
        this.skill = CharacterSkill.getSkill(matcher.group(2));
        this.level = Text.literal(matcher.group(3)).setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.equipSkillReq";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, equipName, skill, level).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
