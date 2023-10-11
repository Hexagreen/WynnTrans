package net.hexagreen.wynntrans.chat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NpcDialogSelection extends NpcDialog implements FocusTextInterface {
    private final Text fullText;

    NpcDialogSelection(MutableText text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.fullText = text;
    }

    public static NpcDialogSelection of(Text text, Pattern regex) {
        return new NpcDialogSelection((MutableText) text, regex);
    }

    @Override
    protected void build() {
        super.build();
        resultText = setToSelectOption(resultText, fullText, pKeyDialog);
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void print() {
        build();
        MinecraftClient.getInstance().inGameHud.getChatHud().clear(false);
        MinecraftClient.getInstance().player.sendMessage(resultText);
    }
}
