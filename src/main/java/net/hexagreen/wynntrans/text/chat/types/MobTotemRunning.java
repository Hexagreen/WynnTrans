package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class MobTotemRunning extends WynnChatText {
    private final Text playerName;
    private final Text link;

    public MobTotemRunning(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = getPlayerName(matcher.group(1));
        this.link = getSibling(3);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.mobTotemRunning";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA));
        resultText.append(newTranslate(parentKey, playerName, link));
    }

    private Text getPlayerName(String string) {
        String name = string + "'s";
        return newTranslate(name).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
    }
}
