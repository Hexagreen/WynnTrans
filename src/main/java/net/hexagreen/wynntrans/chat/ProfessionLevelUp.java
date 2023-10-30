package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ProfessionLevelUp extends WynnChatText {
    private final String level;
    private final Text plyerName;
    private final String professionIcon;
    private final String professionName;

    protected ProfessionLevelUp(Text text, Pattern regex) {
        super(text, regex);
        this.level = matcher.group(2);
        this.professionIcon = "§f" + matcher.group(3);
        this.professionName = matcher.group(4);
        if(inputText.getSiblings().size() == 4) {
            this.plyerName = getPlayerNameFromSibling(0);
        }
        else {
            this.plyerName = Text.literal(matcher.group(1)).setStyle(getStyle(0));
        }
    }

    public static ProfessionLevelUp of(Text text, Pattern regex) {
        return new ProfessionLevelUp(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "professionLevelUp";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(plyerName)
                .append(newTranslate(parentKey, level, professionIcon, professionName)).setStyle(getStyle(0));
    }
}