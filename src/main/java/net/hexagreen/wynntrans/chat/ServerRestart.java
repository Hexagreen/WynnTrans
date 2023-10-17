package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ServerRestart extends WynnChatText {
    private final String number;
    private final String unit;

    protected ServerRestart(Text text, Pattern regex) {
        super(text, regex);
        this.number = matcher.group(1);
        this.unit = matcher.group(2);
    }

    public static ServerRestart of(Text text, Pattern regex) {
        return new ServerRestart(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "serverRestart";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "_" + unit, number).setStyle(getStyle(0)));
    }
}
