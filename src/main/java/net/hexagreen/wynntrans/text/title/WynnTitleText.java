package net.hexagreen.wynntrans.text.title;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.title.types.SimpleTitle;
import net.minecraft.text.Text;

public abstract class WynnTitleText extends WynnTransText {
    public WynnTitleText(Text text) {
        super(text);
    }

    public Text text() {
        try {
            build();
            return resultText;
        }
        catch(IndexOutOfBoundsException e) {
            WynnTrans.LOGGER.warn("IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OOB - Title");
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed title message has been recorded.\n", e);
            return new SimpleTitle(inputText).text();
        }
        return inputText;
    }
}
