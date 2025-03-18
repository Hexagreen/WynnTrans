package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(DrawContext.class)
abstract public class MixinDrawContext {
    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/util/Identifier;)V", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public List<Text> mixinDrawTooltip(List<Text> text) {
        if(MinecraftClient.getInstance().options.advancedItemTooltips) return text;
        return WynnTrans.drawTooltipHandler.translateTooltipText(text);
    }
}
