package net.hexagreen.wynntrans.text.title;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class TitleHandler {
    private boolean recordMode;

    public TitleHandler() {
        this.recordMode = false;
    }

    public void toggleRecordMode() {
        this.recordMode = !recordMode;
        if(recordMode)
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.translatable("wytr.command.titleForceRecordMode.enable"));
        else
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.translatable("wytr.command.titleForceRecordMode.disable"));
    }

    public Text translateTitleText(Text text) {
        if(recordMode) debugClass.writeTextAsJSON(text, "TitleRecord");
        try {
            return TitleType.findAndRun(text, true);
        }
        catch(Exception e) {
            debugClass.writeTextAsJSON(text, "TitleException");
            LogUtils.getLogger().error("[WynnTrans] Exception thrown in translating title texts\n", e);
            return text;
        }
    }

    public Text translateSubtitleText(Text text) {
        if(recordMode) debugClass.writeTextAsJSON(text, "SubtitleRecord");
        try {
            return TitleType.findAndRun(text, false);
        }
        catch(Exception e) {
            debugClass.writeTextAsJSON(text, "SubtitleException");
            LogUtils.getLogger().error("[WynnTrans] Exception thrown in translating subtitle texts\n", e);
            return text;
        }
    }
}
