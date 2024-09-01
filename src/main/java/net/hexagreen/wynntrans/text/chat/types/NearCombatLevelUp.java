package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class NearCombatLevelUp extends WynnChatText {
    private final Text level;
    private final Text playerName;

    public NearCombatLevelUp(Text text, Pattern regex) {
        super(text, regex);
        this.level = Text.literal(matcher.group(2));
        if(!inputText.getSiblings().isEmpty()) {
            this.playerName = getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GOLD));
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.levelUp.combat";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.GOLD));
        resultText.append(newTranslate(parentKey, playerName, level));
    }
}
