package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NearProfessionLevelUp extends WynnChatText {
    private final String level;
    private final Text playerName;
    private final Text profession;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§6(.+) is now level (\\d+) in §f(.)§6 (.+)$").matcher(text.getString()).find();
    }

    public NearProfessionLevelUp(Text text) {
        super(text);
        Matcher matcher = Pattern.compile("^§6(.+) is now level (\\d+) in §f(.)§6 (.+)$").matcher(inputText.getString());
        boolean ignore = matcher.find();
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
    protected String setTranslationKey() {
        return rootKey + "func.levelUp.profession";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, playerName, level, profession)).setStyle(Style.EMPTY.withColor(Formatting.GOLD));
    }
}
