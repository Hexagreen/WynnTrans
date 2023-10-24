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
    protected MutableText gluedDialog;
    protected Pattern regex;

    protected TextGlue(Pattern regex) {
        this.gluedDialog = Text.empty();
        this.regex = regex;
    }

    public abstract boolean push(Text text);

    protected void pop(Class<? extends WynnChatText> wct) {
        try {
            wct.cast(wct.getMethod("of", Text.class, Pattern.class).invoke(null, gluedDialog, regex)).print();
            reset();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException ignore) {
            LOGGER.warn("TextGlue Error: {}", wct.getName());
        }
    }

    protected void register(TextGlue glue) {
        WynnTrans.incomeTextHandler.attachGlue(glue);
    }

    private void reset() {
        this.gluedDialog = Text.empty();
        WynnTrans.incomeTextHandler.removeGlue();
    }
}
