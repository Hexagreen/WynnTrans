package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.CharacterSkill;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EquipmentSkillRequirement extends WynnSystemText {
    private final Text equipName;
    private final Text skill;
    private final Text level;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() < 3) return false;
        return text.getSiblings().get(1).getString().matches(" Your .+ skill must be at least ");
    }

    public EquipmentSkillRequirement(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("Your (.+) skill").matcher(getContentString(0));
        boolean ignore = matcher.find();
        this.equipName = new ItemName(getSibling(-1).getString().replaceAll("\\n", ""))
                .textAsMutable().setStyle(Style.EMPTY.withColor(Formatting.RED));
        this.skill = CharacterSkill.getSkill(matcher.group(1));
        this.level = getSibling(1);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.equipSkillReq";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)).append(header);
        resultText.append(newTranslateWithSplit(translationKey, skill, level, equipName));
    }
}
