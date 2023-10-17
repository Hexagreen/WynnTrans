package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ServerRestarting extends WynnChatText {
    protected ServerRestarting(Text text, Pattern regex) {
        super(text, regex);
    }

    public static ServerRestarting of(Text text, Pattern regex) {
        return new ServerRestarting(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "restarting";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey).setStyle(getStyle(0)));
    }
}
