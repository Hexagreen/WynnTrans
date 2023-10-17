package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class CombatLevelUp extends WynnChatText {
    private final Text level;
    private final Text playerName;

    protected CombatLevelUp(Text text, Pattern regex) {
        super(text, regex);
        this.level = Text.literal(matcher.group(2));
        if(inputText.getSiblings().size() != 0) {
            this.playerName = getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GOLD));
        }
    }

    public static CombatLevelUp of(Text text, Pattern regex) {
        return new CombatLevelUp(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "combatLevelUp";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(playerName)
                .append(newTranslate(parentKey, level).setStyle(getStyle()));
    }
}
