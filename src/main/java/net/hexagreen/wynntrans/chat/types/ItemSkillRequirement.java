package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnSystemText;
import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ItemSkillRequirement extends WynnSystemText {
    private final Text skill;
    private final Text level;

    public ItemSkillRequirement(Text text, Pattern regex) {
        super(text, regex);
        this.skill = CharacterSkill.getSkill(matcher.group(1));
        this.level = Text.literal(matcher.group(2)).setStyle(Style.EMPTY.withColor(Formatting.RED));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.itemSkillReq";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, UnprocessedChatTypeException {
        resultText = Text.empty().append(header).setStyle(Style.EMPTY.withColor(Formatting.DARK_RED));
        resultText.append(newTranslateWithSplit(parentKey, skill, level));
    }
}
