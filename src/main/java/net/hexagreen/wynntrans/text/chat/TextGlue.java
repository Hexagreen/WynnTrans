package net.hexagreen.wynntrans.text.chat;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.function.Function;

public abstract class TextGlue {
    protected static final Logger LOGGER = LogUtils.getLogger();
    private static final byte TIMER_THRESHOLD = 1;
    protected MutableText gluedText;
    private Function<Text, WynnChatText> wct;
    private byte timer;
    private boolean safetyLock;

    protected TextGlue(Function<Text, WynnChatText> wct) {
        this.gluedText = Text.empty();
        this.wct = wct;
        this.safetyLock = false;
    }

    /**
     * This method must have these routines.<p>
     * To remove this glue: {@code pop() -> return false}<br>
     * To append text : {@code resetTimer() -> gluedText.append(text)}<br>
     * To complete this glue: {@code safeNow() -> pop() -> return true}
     *
     * @param text Text line for glue
     * @return Returns {@code true} if this method complete gluing, {@code false} if ignore its text
     */
    public abstract boolean push(Text text);

    public void timer() {
        if(!gluedText.equals(Text.empty())) {
            if(++this.timer >= TIMER_THRESHOLD) {
                pop();
            }
        }
    }

    protected void pop() {
        reset();
        if(safetyLock) {
            wct.apply(gluedText).print();
        }
    }

    protected void resetTimer() {
        this.timer = 0;
    }

    protected void safeNow() {
        this.safetyLock = true;
    }

    protected void changeWct(Function<Text, WynnChatText> wct) {
        this.wct = wct;
    }

    private void reset() {
        WynnTrans.onGameMessageHandler.removeGlue();
    }
}
