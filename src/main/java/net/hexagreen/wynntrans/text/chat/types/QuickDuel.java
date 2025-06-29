package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ICommonTooltip;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class QuickDuel extends WynnChatText implements ICommonTooltip {
    private final String playerName;
    private final String playerLevel;
    private final String command;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^(.+) would like to duel! Type ").matcher(text.getString()).find();
    }

    public QuickDuel(Text text) {
        super(text);
        this.playerName = inputText.getString().replaceFirst("^(.+) \\[Lv\\. \\d+] would.+", "$1");
        this.playerLevel = inputText.getString().replaceFirst("^.+ \\[Lv\\. (\\d+)] would.+", "$1");
        this.command = "/duel " + playerName;
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.quickDuel";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.translatable(translationKey, playerName, playerLevel, buildCommand()).setStyle(Style.EMPTY.withColor(Formatting.BLUE));
    }

    private Text buildCommand() {
        return Text.literal(command).setStyle(
                Style.EMPTY.withUnderline(true).withColor(Formatting.GOLD)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                        .withHoverEvent(getHoverRunCommand())
        );
    }
}
