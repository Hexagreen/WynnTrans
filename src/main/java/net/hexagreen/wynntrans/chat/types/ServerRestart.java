package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ServerRestart extends WynnChatText {
    private final String number;
    private final String unit;

    public ServerRestart(Text text, Pattern regex) {
        super(text, regex);
        this.number = matcher.group(1);
        this.unit = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "serverRestart";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "." + unit, number).setStyle(getStyle(0)));
    }
}
