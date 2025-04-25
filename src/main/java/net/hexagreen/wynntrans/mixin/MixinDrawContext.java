package net.hexagreen.wynntrans.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DrawContext.class)
abstract public class MixinDrawContext {
    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;Ljava/util/Optional;IILnet/minecraft/util/Identifier;)V",
            at = @At("HEAD"), ordinal = 0, argsOnly = true)
    public List<Text> mixinDrawTooltip(List<Text> text) {
        if(MinecraftClient.getInstance().options.advancedItemTooltips) return text;
        return WynnTrans.drawTooltipHandler.translateTooltipText(text);
    }

    @Final
    @Shadow
    private MatrixStack matrices;

    @Shadow
    public abstract int getScaledWindowWidth();

    @Inject(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER))
    public void mixinModifyMatrixStack(TextRenderer textRenderer, List<TooltipComponent> components, int x, int y,
                                       TooltipPositioner positioner, @Nullable Identifier texture, CallbackInfo ci, @Local(ordinal = 5) int m, @Local(ordinal = 7) int o) {
        if(o < 3) {
            float scale = 1 - ((3f - o) / m);
            this.matrices.translate(-x, -y, 0f);
            this.matrices.scale(scale, scale, 1f);
            this.matrices.translate(x / scale, y / scale, 0f);
        }
    }

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER), ordinal = 6)
    private int modifyVertexX(int value, @Local(ordinal = 0, argsOnly = true) int x, @Local(ordinal = 4) int l, @Local(ordinal = 5) int m, @Local(ordinal = 7) int o) {
        if(o < 3) {
            float scale = 1 - ((3f - o) / m);
            if(this.getScaledWindowWidth() < x + 12 + l) {
                return Math.max((int) (((x - 12) / scale) - l), 4);
            }
            return (int) (value / scale);
        }
        return value;
    }

    @ModifyVariable(method = "drawTooltip(Lnet/minecraft/client/font/TextRenderer;Ljava/util/List;IILnet/minecraft/client/gui/tooltip/TooltipPositioner;Lnet/minecraft/util/Identifier;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;push()V", shift = At.Shift.AFTER), ordinal = 7)
    private int modifyVertexY(int value) {
        return Math.max(value, 3);
    }
}
