package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ProfessionLevelAnnounce extends WynnChatText {
    private final String level;
    private final String profName;
    private final Text playerName;

    protected ProfessionLevelAnnounce(Text text, Pattern regex) {
        super(text, regex);
        this.level = matcher.group(2);
        this.profName = matcher.group(3);
        this.playerName = getPlayerNameFromSibling(4);
    }

    public static ProfessionLevelAnnounce of(Text text, Pattern regex) {
        return new ProfessionLevelAnnounce(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "levelAnnounce.profession";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(0))
                .append(getSibling(1))
                .append(getSibling(2))
                .append(newTranslate(parentKey + "_pre", level, profName))
                .append(playerName)
                .append(newTranslate(parentKey + "_suf", level, profName));
    }
}
