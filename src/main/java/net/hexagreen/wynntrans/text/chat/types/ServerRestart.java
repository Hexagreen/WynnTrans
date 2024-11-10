package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ServerRestart extends WynnChatText {
    private final String number;
    private final String unit;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§cThis world will restart in (\\d+) (minutes?|seconds?)\\.$").matcher(text.getString()).find();
    }

    public ServerRestart(Text text) {
        super(text, Pattern.compile("^§cThis world will restart in (\\d+) (minutes?|seconds?)\\.$"));
        this.number = matcher.group(1);
        this.unit = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.serverRestart";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey + "." + unit, number).setStyle(Style.EMPTY.withColor(Formatting.RED)));
    }
}
