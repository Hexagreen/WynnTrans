package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.enums.Profession;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NearProfessionLevelUp extends WynnChatText {
    private final String level;
    private final Text playerName;
    private final Text profession;

    public NearProfessionLevelUp(Text text, Pattern regex) {
        super(text, regex);
        this.level = matcher.group(2);
        this.profession = Profession.getProfession(matcher.group(3).charAt(0)).getText().setStyle(getStyle(0));
        if(inputText.getSiblings().size() == 4) {
            this.playerName = getPlayerNameFromSibling(0);
        }
        else {
            this.playerName = Text.literal(matcher.group(1)).setStyle(getStyle(0));
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "levelUp.profession";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(playerName)
                .append(newTranslate(parentKey, level, profession)).setStyle(getStyle(0));
    }
}
