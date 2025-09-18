package net.hexagreen.wynntrans.text.chat;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.WynnTransText;
import net.hexagreen.wynntrans.text.chat.types.SimpleText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.function.Function;

public abstract class TextGlue {
    private static final byte TIMER_THRESHOLD = 1;
    protected MutableText gluedText;
    private Function<Text, WynnChatText> wct;
    private byte timer;
    private boolean safetyLock;

    public static Text colorCodedToStyledGluedText(Text text) {
        MutableText result = Text.empty();
        for(Text line : text.getSiblings()) {
            result.append(WynnTransText.colorCodedToStyled(line));
        }
        return result;
    }

    protected TextGlue(Function<Text, WynnChatText> wct) {
        this.gluedText = Text.empty();
        this.wct = wct;
        this.safetyLock = false;
    }

    /**
     * This method may have these routines.<p>
     * To remove this glue: {@code pop() -> return false}<br>
     * To append text : {@code resetTimer() -> gluedText.append(text)}<br>
     * To complete this glue: {@code safeNow() -> pop() -> return true}
     *
     * @param text Text line for glue
     * @return Returns {@code true} if {@code text} consumed to gluing, {@code false} if ignore its text
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
        else gluedText.getSiblings().forEach(text -> new SimpleText(text).print());
    }

    protected void pop(Object... context) {
        reset();
        if(safetyLock) {
            wct.apply(gluedText).attachGlueContext(context).print();
        }
        else gluedText.getSiblings().forEach(text -> new SimpleText(text).print());
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
