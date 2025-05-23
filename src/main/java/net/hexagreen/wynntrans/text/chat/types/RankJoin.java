package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RankJoin extends WynnChatText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^. .+ has just logged in!$").matcher(text.getString()).find();
    }
    
    public RankJoin(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.rankJoin";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(getSibling(0)).append(getSibling(1)).append(Text.translatable(translationKey).setStyle(getStyle(2)));
    }
}
