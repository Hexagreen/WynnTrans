package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CombatLevelAnnounce extends WynnChatText {
    private final String level;
    private final Text playerName;

    protected CombatLevelAnnounce(Text text, Pattern regex) {
        super(text, regex);
        this.level = matcher.group(2);
        this.playerName = getPlayerNameFromSibling(4);
    }

    public static CombatLevelAnnounce of(Text text, Pattern regex) {
        return new CombatLevelAnnounce(text, regex);
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
                .append(newTranslate(parentKey + "_pre", level))
                .append(playerName)
                .append(newTranslate(parentKey + "_suf", level));
    }
}