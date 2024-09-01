package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class DrawTooltipHandler {

    public List<Text> translateTooltipText(List<Text> text) {
        if(GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_KP_ADD) == 1) {
            WynnTrans.wynnTranslationStorage.recordUnregisteredTooltip(text, "Tooltip");
        }
        return text;
    }

    public Text translateTooltipText(Text text) {
        if(GLFW.glfwGetKey(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_KP_ADD) == 1) {
            debugClass.writeTextAsJSON(Text.literal(" "));
            debugClass.writeTextAsJSON(text, "Tooltip1");
            debugClass.writeTextAsJSON(Text.literal(" "));
        }
        return text;
    }
}
