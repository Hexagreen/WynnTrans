package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class SimpleTooltip extends WynnTooltipText {

    public SimpleTooltip(List<Text> text) {
        super(text);
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = inputText;
        textRecorder();
    }
}
