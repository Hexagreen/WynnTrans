package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.regex.Pattern;

public class SimpleTooltip extends WynnTooltipText {
    public SimpleTooltip(List<Text> text) {
        super(text);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "tooltip.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        for(Text text : getSiblings()) {
            if(hasTranslation(text) || pressedAddTranslationKey()) {
                resultText.append(applyTranslation(text));
            }
            else {
                resultText.append(text);
            }
        }
        textRecorder(resultText.getSiblings());
    }

    private boolean hasTranslation(Text text) {
        String hash = DigestUtils.sha1Hex(text.getString());
        String key = parentKey + hash;
        if(WTS.checkTranslationDoNotRegister(key)) return true;

        int index = 0;
        List<Text> siblings = text.getSiblings();
        for(int i = 0; i < siblings.size(); i++) {
            if(Pattern.compile("[A-z]").matcher(siblings.get(i).getString()).find()) {
                index = i;
                break;
            }
        }
        return WTS.checkTranslationDoNotRegister(key + "_" + (index + 1));
    }

    private boolean pressedAddTranslationKey() {
        long handle = MinecraftClient.getInstance().getWindow().getHandle();
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_KP_MULTIPLY) == 1
                && GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_LEFT_CONTROL) == 1;
    }

    private Text applyTranslation(Text text) {
        MutableText result = Text.empty();
        String hash = DigestUtils.sha1Hex(text.getString());

        List<Text> siblings = text.getSiblings();
        for(int i = 0; i < siblings.size(); i++) {
            Text sibling = siblings.get(i);

            String content = sibling.getString();
            Style style = sibling.getStyle();
            String key = parentKey + hash;
            if(siblings.size() != 1) key = key + "_" + (i + 1);

            if(!Pattern.compile("[A-z]").matcher(content).find()) {
                result.append(sibling);
                continue;
            }

            if(WTS.checkTranslationExist(key, content)) {
                result.append(newTranslate(key).setStyle(style));
            }
            else {
                result.append(sibling);
            }
        }
        return result;
    }
}
