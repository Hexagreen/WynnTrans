package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipAppender;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;

@Mixin(value = ItemStack.class)
abstract public class MixinItemStack {
    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    <T extends TooltipAppender> void appendTooltipMixin(ComponentType<T> componentType, Item.TooltipContext context, Consumer<Text> textConsumer, TooltipType type, CallbackInfo ci) {
        if(!WynnTrans.drawTooltipHandler.getShowOriginal() && componentType != DataComponentTypes.LORE) {
            ci.cancel();
        }
    }

    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/Item;appendTooltip(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/Item$TooltipContext;Ljava/util/List;Lnet/minecraft/item/tooltip/TooltipType;)V"))
    void item$appendTooltipMixin(Item instance, ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        if(WynnTrans.drawTooltipHandler.getShowOriginal()) {
            instance.appendTooltip(stack, context, tooltip, type);
        }
    }

    @Inject(method = "appendAttributeModifiersTooltip", at = @At(value = "HEAD"), cancellable = true)
    void appendAttributeModifiersTooltipMixin(Consumer<Text> textConsumer, @Nullable PlayerEntity player, CallbackInfo ci) {
        if(!WynnTrans.drawTooltipHandler.getShowOriginal()) {
            ci.cancel();
        }
    }
}