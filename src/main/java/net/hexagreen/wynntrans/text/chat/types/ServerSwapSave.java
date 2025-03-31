package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ServerSwapSave extends WynnChatText {
    private final Text worldChannel;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§7Saving your player data before switching to §f(?:NA|EU|AS)\\d+").matcher(text.getString()).find();
    }

    public ServerSwapSave(Text text) {
        super(text);
        String channelNum = getContentString().replaceAll(".+ switching to ", "").replace("§7...", "");
        this.worldChannel = Text.literal(channelNum);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.serverSwapSave";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, worldChannel).setStyle(GRAY));
    }
}
