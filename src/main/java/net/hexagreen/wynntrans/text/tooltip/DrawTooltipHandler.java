package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class DrawTooltipHandler {
    private List<Text> cacheKey;
    private List<Text> cacheVal;
    private boolean recordMode;

    public DrawTooltipHandler() {
        this.cacheKey = new ArrayList<>();
        this.cacheVal = new ArrayList<>();
    }

    public void clearCache() {
        this.cacheKey = new ArrayList<>();
        this.cacheVal = new ArrayList<>();
    }

    public void toggleRecordMode() {
        this.recordMode = !recordMode;
        if(recordMode)
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.translatable("wytr.command.tooltipForceRecordMode.enable"));
        else
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(Text.translatable("wytr.command.tooltipForceRecordMode.disable"));
    }

    public List<Text> translateTooltipText(List<Text> texts) {
        try {
            return getCacheOrTranslate(texts);
        }
        catch(Exception e) {
            for(Text text : texts) {
                debugClass.writeTextAsJSON(text, "TooltipException");
            }
            WynnTrans.LOGGER.error("Exception thrown in translating tooltip texts\n", e);
            return texts;
        }
    }

    private List<Text> getCacheOrTranslate(List<Text> texts) {
        if(!texts.equals(cacheKey)) {
            if(recordMode) WynnTrans.wynnTranslationStorage.recordUnregisteredTooltip(texts, "Tooltip");
            cacheKey = texts;
            cacheVal = TooltipType.findAndRun(texts);
        }
        return cacheVal;
    }
}
