package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class Shout extends WynnChatText {
    private final String name;
    private final String server;
    private final String body;

    public Shout(Text text, Pattern regex) {
        super(text, regex);
        this.name = matcher.group(1);
        this.server = matcher.group(2);
        this.body = text.getString().replace(regex.pattern(), "");
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "shout";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, name, server).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)));
        resultText.append(body).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
    }
}
