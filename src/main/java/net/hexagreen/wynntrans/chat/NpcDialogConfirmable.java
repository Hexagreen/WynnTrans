package net.hexagreen.wynntrans.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NpcDialogConfirmable extends FocusText {
    NpcDialogConfirmable(MutableText text, Pattern regex) {
        super(text, regex);
    }

    public static NpcDialogConfirmable of(Text text, Pattern regex) {
        return new NpcDialogConfirmable((MutableText) text, regex);
    }

    @Override
    protected void build() {
        super.build();
        setToPressShift();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void print() {
        build();
        MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
        MinecraftClient.getInstance().player.sendMessage(resultText);
    }
}
