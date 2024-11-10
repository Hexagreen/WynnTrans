package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ShoutLiteral extends WynnChatText {
    private final String name;
    private final String server;
    private final String body;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§5(.+) \\[WC(\\d+)] shouts: §d").matcher(text.getString()).find();
    }

    public ShoutLiteral(Text text) {
        super(text, Pattern.compile("^§5(.+) \\[WC(\\d+)] shouts: §d"));
        this.name = matcher.group(1);
        this.server = matcher.group(2);
        this.body = text.getString().replaceAll("^§5(.+) \\[WC(\\d+)] shouts: §d", "");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.shout";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, name, server).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)));
        resultText.append(body).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
    }
}
