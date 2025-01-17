package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class Shout extends WynnChatText {
    private final String name;
    private final String server;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(.+) \\[((?:NA|EU)\\d+)] shouts: ").matcher(text.getString()).find();
    }

    public Shout(Text text) {
        super(text);
        this.name = inputText.getString().replaceFirst("^(.+) \\[.+", "$1");
        this.server = inputText.getString().replaceFirst(".+\\[((?:NA|EU)\\d+)].+", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.shout";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, name, server).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)));
        for(int i = 0; i < getSiblings().size(); i++) {
            if(getSibling(i).getString().contains("] shouts: ")) continue;
            resultText.append(getSibling(i));
        }
    }
}
