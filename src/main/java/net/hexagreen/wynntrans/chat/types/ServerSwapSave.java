package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ServerSwapSave extends WynnChatText {
    private final Text worldChannel;

    public ServerSwapSave(Text text, Pattern regex) {
        super(text, regex);
        this.worldChannel = getSibling(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "serverSwapSave";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, worldChannel));
    }
}
