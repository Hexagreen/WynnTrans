package net.hexagreen.wynntrans.text.display;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.display.types.SimpleDisplay;
import net.minecraft.text.Text;

public abstract class WynnDisplayText extends WynnTransText {
    public WynnDisplayText(Text text) {
        super(text);
    }

    public Text text() {
        try {
            build();
            return resultText;
        }
        catch(IndexOutOfBoundsException e) {
            WynnTrans.LOGGER.warn("IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OOB - Display");
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed display message has been recorded.\n", e);
            return new SimpleDisplay(inputText).text();
        }
        return inputText;
    }
}
