package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class SkillCooldown extends WynnChatText {
    protected SkillCooldown(Text text, Pattern regex) {
        super(text, regex);
    }

    public static SkillCooldown of(Text text, Pattern regex) {
        return new SkillCooldown(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "skillCooldown";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(0))
                .append(getSibling(1))
                .append(getSibling(2))
                .append(getSibling(3))
                .append(newTranslate(parentKey).setStyle(getStyle(4)));
    }
}
