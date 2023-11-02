package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class EquipmentSkillRequirement extends WynnChatText {

    private final Text skill;
    private final Text level;

    protected EquipmentSkillRequirement(Text text, Pattern regex) {
        super(text, regex);
        this.skill = CharacterSkill.getSkill(matcher.group(1));
        this.level = Text.literal(
                getSibling(2).getString().replace(".", ""))
                .setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    public static EquipmentSkillRequirement of(Text text, Pattern regex) {
        return new EquipmentSkillRequirement(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "equipSkillReq";
    }

    @Override
    protected void build() {
        Text equipName = getSibling(0);

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, equipName, skill, level).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}