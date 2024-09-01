package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(DrawContext.class)
abstract public class MixinDrawContext {

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;II)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public List<Text> mixinDrawTooltip0(List<Text> text) {
        return WynnTrans.drawTooltipHandler.translateTooltipText(text);
    }

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;II)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public Text mixinDrawTooltip1(Text text) {
        return WynnTrans.drawTooltipHandler.translateTooltipText(text);
    }

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;II)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public List<Text> mixinDrawTooltip2(List<Text> text) {
        return WynnTrans.drawTooltipHandler.translateTooltipText(text);
    }
}
