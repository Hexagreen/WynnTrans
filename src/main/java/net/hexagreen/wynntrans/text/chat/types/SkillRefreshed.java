package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class SkillRefreshed extends WynnChatText {
    private static final String icon = "§8[§7⬤§8] ";

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§.\\[§.⬤§.] §.(.+)§. has been refreshed!$").matcher(text.getString()).find();
    }

    public SkillRefreshed(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.skillRefreshed";
    }

    @Override
    protected void build() {
        resultText = Text.literal(icon);

        String _skill = inputText.getString().replaceFirst("^§.\\[§.⬤§.] §.(.+)§. has been refreshed!$", "$1");
        String nSkill = normalizeStringForKey(_skill);
        Text skillName;
        if(WTS.checkTranslationDoNotRegister("wytr.ability.node." + nSkill))
            skillName = Text.translatable("wytr.ability.node." + nSkill).setStyle(GRAY);
        else if(WTS.checkTranslationDoNotRegister("wytr.ability.object." + nSkill))
            skillName = Text.translatable("wytr.ability.object." + nSkill).setStyle(GRAY);
        else skillName = Text.literal(_skill).setStyle(GRAY);

        resultText.append(Text.translatable(translationKey, skillName).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
    }
}
