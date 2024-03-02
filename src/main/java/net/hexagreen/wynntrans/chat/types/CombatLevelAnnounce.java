package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class CombatLevelAnnounce extends WynnChatText {
    private final String level;
    private final Text playerName;

    public CombatLevelAnnounce(Text text, Pattern regex) {
        super(text, regex);
        this.level = "§f" + matcher.group(2);
        this.playerName = getPlayerNameFromSibling(4);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "levelAnnounce.combat";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(0))
                .append(getSibling(1))
                .append(getSibling(2))
                .append(newTranslate(parentKey, playerName, level).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
