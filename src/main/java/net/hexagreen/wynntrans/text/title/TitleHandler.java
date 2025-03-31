package net.hexagreen.wynntrans.text.title;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.minecraft.text.Text;

public class TitleHandler {
    private boolean recordMode;

    public TitleHandler() {
        this.recordMode = false;
    }

    public void toggleRecordMode() {
        this.recordMode = !recordMode;
        if(recordMode)
            WynnTransText.transportMessage(Text.translatable("wytr.command.titleForceRecordMode.enable"));
        else
            WynnTransText.transportMessage(Text.translatable("wytr.command.titleForceRecordMode.disable"));
    }

    public Text translateTitleText(Text text) {
        if(recordMode) debugClass.writeTextAsJSON(text, "TitleRecord");
        try {
            return TitleType.findAndRun(text, true);
        }
        catch(Exception e) {
            debugClass.writeTextAsJSON(text, "TitleException");
            WynnTrans.LOGGER.error("Exception thrown in translating title texts\n", e);
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
            WynnTrans.LOGGER.error("Exception thrown in translating subtitle texts\n", e);
            return text;
        }
    }
}
