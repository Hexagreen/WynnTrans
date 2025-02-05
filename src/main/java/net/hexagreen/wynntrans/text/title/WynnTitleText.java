package net.hexagreen.wynntrans.text.title;

import com.mojang.logging.LogUtils;
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
            LogUtils.getLogger().warn("[WynnTrans] IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OOB - Title");
        }
        catch(TextTranslationFailException e) {
            LogUtils.getLogger().warn("[WynnTrans] Unprocessed title message has been recorded.\n", e);
            return new SimpleTitle(inputText).text();
        }
        return inputText;
    }
}
