package net.hexagreen.wynntrans.text.display;

import net.hexagreen.wynntrans.enums.DisplayType;
import net.minecraft.text.Text;

public class DisplayEntityHandler {

    public Text translateDisplayText(Text text) {
        return DisplayType.findAndRun(text);
    }
}
