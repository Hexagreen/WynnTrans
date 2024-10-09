package net.hexagreen.wynntrans.text.display;

import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.Text;

public class DisplayEntityHandler {

    public Text translateDisplayText(Text text) {
        try {
            return DisplayType.findAndRun(text);
        }
        catch(Exception e) {
            debugClass.writeTextAsJSON(text, "DisplayException");
            return text;
        }
    }
}
