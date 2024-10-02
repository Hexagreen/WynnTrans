package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
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
        this.playerName = Text.literal(matcher.group(1));
        if(!text.getSiblings().isEmpty()) throw new TextTranslationFailException("CombatLevelAnnounce.class");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.levelAnnounce.combat";
    }

    @Override
    protected void build() {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.GRAY));
        resultText.append(Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))).append(Text.literal("!")).append(Text.literal("] ").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))).append(newTranslate(parentKey, playerName, level));
    }
}
