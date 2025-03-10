package net.hexagreen.wynntrans.text.scoreboard.types;

import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.scoreboard.WynnScoreboardText;
import net.minecraft.text.Text;

import java.util.List;

public class SimpleScoreboardContent extends WynnScoreboardText {
    private final List<Text> originalText;

    public SimpleScoreboardContent(List<Text> texts) {
        super(texts);
        this.originalText = texts;
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = inputText;

        debugClass.writeString2File("", "json.txt");
        for(Text line : originalText) debugClass.writeTextAsJSON(line, "Scoreboard");
        debugClass.writeString2File("", "json.txt");
    }
}
