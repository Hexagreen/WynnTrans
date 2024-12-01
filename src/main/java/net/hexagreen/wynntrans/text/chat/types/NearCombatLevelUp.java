package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class NearCombatLevelUp extends WynnChatText {
    private final Text level;
    private final Text playerName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(.+) is now combat level (\\d+)$").matcher(text.getString()).find();
    }

    public NearCombatLevelUp(Text text) {
        super(text, Pattern.compile("^(.+) is now combat level (\\d+)$"));
        this.level = Text.literal(matcher.group(2));
        if(!getSiblings().isEmpty()) {
            this.playerName = getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GOLD));
        }
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.levelUp.combat";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.GOLD));
        resultText.append(Text.translatable(translationKey, playerName, level));
    }
}
