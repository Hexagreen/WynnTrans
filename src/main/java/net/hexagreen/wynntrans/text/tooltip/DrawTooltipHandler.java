package net.hexagreen.wynntrans.text.tooltip;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class DrawTooltipHandler {
    private static boolean lever = false;
    private static boolean registered = true;

    public List<Text> translateTooltipText(List<Text> texts) {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_ADD) == 0) lever = false;
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_ADD) == 1 && !lever) registered = false;
        if(!registered) {
            WynnTrans.wynnTranslationStorage.recordUnregisteredTooltip(texts, "Tooltip");
            registered = true;
            lever = true;
        }
        return texts;
    }

    public Text translateTooltipText(Text text) {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_ADD) == 0) lever = false;
        if(GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_ADD) == 1 && !lever) registered = false;
        if(!registered) {
            debugClass.writeTextAsJSON(Text.literal(" "));
            debugClass.writeTextAsJSON(text, "Tooltip1");
            debugClass.writeTextAsJSON(Text.literal(" "));
            registered = true;
            lever = true;
        }
        return text;
    }
}
