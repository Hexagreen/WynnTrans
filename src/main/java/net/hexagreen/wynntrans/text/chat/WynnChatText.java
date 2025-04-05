package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.chat.types.SimpleText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Locale;

import static net.hexagreen.wynntrans.text.WynntilsCompatible.tossToWynntilsTextHandleEvent;

public abstract class WynnChatText extends WynnTransText {
    public WynnChatText(Text text) {
        super(text);
    }

    public boolean print() {
        Text printLine = text();
        if(printLine == null) return true;
        if(WynnTrans.wynntilsLoaded) {
            if(tossToWynntilsTextHandleEvent(printLine))
                return true;
        }
        transportMessage(printLine);
        return true;
    }

    public MutableText text() {
        try {
            build();
            return resultText;
        }
        catch(IndexOutOfBoundsException e) {
            WynnTrans.LOGGER.warn("IndexOutOfBound occurred.\n", e);
            debugClass.writeTextAsJSON(inputText, "OutOfBound");
        }
        catch(TextTranslationFailException e) {
            WynnTrans.LOGGER.warn("Unprocessed chat message has been recorded.\n", e);
            return new SimpleText(inputText).text();
        }
        return inputText;
    }

    /**
     * Get player name from sibling.
     * <p>
     * This method detects sibling would contain Custom nickname (Champion Feature) or not.
     *
     * @param index Index of sibling contains Custom Nickname form text
     * @return Custom nickname or Original name as {@code Text}
     */
    protected Text getPlayerNameFromSibling(int index) {
        String name = getContentString(index);
        if("".equals(name)) {
            return getSibling(index).getSiblings().getFirst();
        }
        else {
            return getSibling(index);
        }
    }

    protected String capitalizeFirstChar(String input) {
        String body = input.substring(1);
        char head = input.toUpperCase(Locale.ENGLISH).charAt(0);

        return head + body;
    }

}
