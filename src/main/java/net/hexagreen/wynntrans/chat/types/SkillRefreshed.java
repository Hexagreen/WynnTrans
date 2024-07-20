package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class SkillRefreshed extends WynnChatText {
    private final String skillName;
    private static final String icon = "§8[§7⬤§8] ";

    public SkillRefreshed(Text text, Pattern regex) {
        super(text, regex);
        this.skillName = "§7" + matcher.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "skillRefreshed";
    }

    @Override
    protected void build() {
        resultText = Text.literal(icon);
        resultText.append(newTranslate(parentKey, skillName).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
    }
}
