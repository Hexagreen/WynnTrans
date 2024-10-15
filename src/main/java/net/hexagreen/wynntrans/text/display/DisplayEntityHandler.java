package net.hexagreen.wynntrans.text.display;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.Text;

public class DisplayEntityHandler {

    public Text translateDisplayText(Text text) {
        try {
            return DisplayType.findAndRun(text);
        }
        catch(Exception e) {
            debugClass.writeTextAsJSON(text, "DisplayException");
            LogUtils.getLogger().error("[WynnTrans] Exception thrown in translating display texts\n", e);
            return text;
        }
    }
}
