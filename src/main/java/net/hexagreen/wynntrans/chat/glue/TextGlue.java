package net.hexagreen.wynntrans.chat.glue;

import com.mojang.logging.LogUtils;
import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Pattern;

public abstract class TextGlue {
    protected static final Logger LOGGER = LogUtils.getLogger();
    private static final byte TIMER_THRESHOLD = 2;
    protected MutableText gluedText;
    protected final Pattern regex;
    protected final Class<? extends WynnChatText> wctClass;
    private byte timer;
    private boolean safetyLock;

    protected TextGlue(Pattern regex, Class<? extends WynnChatText> wctClass) {
        this.gluedText = Text.empty();
        this.regex = regex;
        this.wctClass = wctClass;
        this.safetyLock = false;
    }

    /**
     * This method must have these routines.<p>
     * To remove this glue: {@code pop() -> return false}<br>
     * To append text : {@code resetTimer() -> gluedText.append(text)}<br>
     * To complete this glue: {@code safeNow() -> pop() -> return true}
     * @param text Text line for glue
     * @return Returns {@code true} if this method complete gluing, {@code false} if ignore its text
     */
    public abstract boolean push(Text text);

    public void timer() {
        if(!gluedText.equals(Text.empty())) {
            if (++this.timer >= TIMER_THRESHOLD) {
                pop();
            }
        }
    }

    protected void pop() {
        if(safetyLock){
            try {
                wctClass.cast(wctClass.getMethod("of", Text.class, Pattern.class).invoke(null, gluedText, regex)).print();
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                LOGGER.error("TextGlue Error:", e);
            }
        }
        reset();
    }

    protected void resetTimer() {
        this.timer = 0;
    }

    protected void safeNow(){
        this.safetyLock = true;
    }

    private void reset() {
        this.gluedText = Text.empty();
        WynnTrans.incomeTextHandler.removeGlue();
    }
}