package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ServerSwapSave extends WynnChatText {
    private final Text worldChannel;

    public ServerSwapSave(Text text, Pattern regex) {
        super(text, regex);
        String channelNum = getContentString().replaceAll(".+ switching to ", "").replace("§7...", "");
        this.worldChannel = Text.literal(channelNum);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.serverSwapSave";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, worldChannel).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
