package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class SpeedBoost extends WynnChatText {
    private final String duration;

    public SpeedBoost(Text text, Pattern regex) {
        super(text, regex);
        this.duration = matcher.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.speedBoost";
    }

    @Override
    protected void build() {
        Text buffTime = Text.translatable(parentKey + ".dur", duration).setStyle(Style.EMPTY.withColor(Formatting.AQUA));

        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, buffTime).setStyle(getStyle(1)));
    }
}
