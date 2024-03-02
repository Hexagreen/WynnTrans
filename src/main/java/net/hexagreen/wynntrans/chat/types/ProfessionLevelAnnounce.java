package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ProfessionLevelAnnounce extends WynnChatText {
    private final String level;
    private final Text playerName;
    private final Text profession;

    public ProfessionLevelAnnounce(Text text, Pattern regex) {
        super(text, regex);
        this.level = "§f" + matcher.group(2);
        String profIcon = matcher.group(3) + " ";
        String keyProfName = "wytr.profession." + matcher.group(4).toLowerCase();
        this.playerName = getPlayerNameFromSibling(4);
        this.profession = Text.literal(profIcon).setStyle(Style.EMPTY.withColor(Formatting.WHITE))
                .append(Text.translatable(keyProfName).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
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
                .append(newTranslate(parentKey, playerName, level, profession).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
