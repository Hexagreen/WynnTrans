package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class SkillRefreshed extends WynnChatText {
    private static final String icon = "§8[§7⬤§8] ";
    private final String skillName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§.\\[§.⬤§.] §.(.+)§. has been refreshed!$").matcher(text.getString()).find();
    }

    public SkillRefreshed(Text text) {
        super(text);
        this.skillName = "§7" + inputText.getString().replaceFirst("^§.\\[§.⬤§.] §.(.+)§. has been refreshed!$", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.skillRefreshed";
    }

    @Override
    protected void build() {
        resultText = Text.literal(icon);
        resultText.append(Text.translatable(translationKey, skillName).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
    }
}
