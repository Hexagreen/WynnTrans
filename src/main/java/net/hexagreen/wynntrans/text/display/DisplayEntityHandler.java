package net.hexagreen.wynntrans.text.display;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.minecraft.text.Text;

public class DisplayEntityHandler {
    private boolean recordMode;

    public DisplayEntityHandler() {
        this.recordMode = false;
    }

    public void toggleRecordMode() {
        this.recordMode = !recordMode;
        if(recordMode)
            WynnTransText.transportMessage(Text.translatable("wytr.command.displayForceRecordMode.enable"));
        else
            WynnTransText.transportMessage(Text.translatable("wytr.command.displayForceRecordMode.disable"));
    }

    public Text translateDisplayText(Text text) {
        if(recordMode) debugClass.writeTextAsJSON(text, "DisplayRecord");
        try {
            return DisplayType.findAndRun(text);
        }
        catch(Exception e) {
            debugClass.writeTextAsJSON(text, "DisplayException");
            WynnTrans.LOGGER.error("Exception thrown in translating display texts\n", e);
            return text;
        }
    }
}
