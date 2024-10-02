package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class NearProfessionLevelUp extends WynnChatText {
    private final String level;
    private final Text playerName;
    private final Text profession;

    public NearProfessionLevelUp(Text text, Pattern regex) {
        super(text, regex);
        this.level = matcher.group(2);
        this.profession = Profession.getProfession(matcher.group(3).charAt(0)).getTextWithIcon().setStyle(Style.EMPTY.withColor(Formatting.GOLD));
        if(!getSiblings().isEmpty()) {
            throw new TextTranslationFailException("NearProfessionLevelUp.class");
        }
        else {
            this.playerName = Text.literal(matcher.group(1));
        }
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.levelUp.profession";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, playerName, level, profession)).setStyle(Style.EMPTY.withColor(Formatting.GOLD));
    }
}
