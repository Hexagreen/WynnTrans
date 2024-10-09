package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ICommonTooltip;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class QuickTrade extends WynnChatText implements ICommonTooltip {
    private final String playerName;
    private final String command;

    public QuickTrade(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = matcher.group(1);
        this.command = "/trade " + playerName;
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.quickTrade";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.translatable(parentKey, playerName, buildCommand()).setStyle(getStyle());
    }

    private Text buildCommand() {
        return Text.literal(command).setStyle(Style.EMPTY.withUnderline(true).withColor(Formatting.GOLD).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)).withHoverEvent(getHoverRunCommand()));
    }
}
