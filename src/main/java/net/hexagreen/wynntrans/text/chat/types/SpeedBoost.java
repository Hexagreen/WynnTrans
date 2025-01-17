package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class SpeedBoost extends WynnChatText {
    private final String duration;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\+(\\d) minutes speed boost\\.").matcher(text.getString()).find();
    }

    public SpeedBoost(Text text) {
        super(text);
        this.duration = inputText.getString().replaceAll("\\D", "");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.speedBoost";
    }

    @Override
    protected void build() {
        Text buffTime = Text.translatable(translationKey + ".dur", duration).setStyle(Style.EMPTY.withColor(Formatting.AQUA));

        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, buffTime).setStyle(getStyle(1)));
    }
}
