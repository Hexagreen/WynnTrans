package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ServerRestart extends WynnChatText {
    private final String timer;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§cThis world will restart in (\\d+ (?:minutes?|seconds?))\\.$").matcher(text.getString()).find();
    }

    public ServerRestart(Text text) {
        super(text);
        this.timer = inputText.getString().replaceFirst(".+in (\\d+) (minutes?|seconds?).+", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.serverRestart";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, ITime.translateTime(timer)).setStyle(Style.EMPTY.withColor(Formatting.RED)));
    }
}
