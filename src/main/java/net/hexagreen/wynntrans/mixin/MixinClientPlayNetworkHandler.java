package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class MixinClientPlayNetworkHandler {
    @Shadow private ClientWorld world;

    @Inject(method = "onEntityStatusEffect", at = @At("TAIL"))
    public void mixinOnEntityStatusEffect(EntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        if(world.getEntityById(packet.getEntityId()) == MinecraftClient.getInstance().player) {
            if(packet.getEffectId() == StatusEffects.SLOWNESS
                    && packet.getAmplifier() == 3
                    && packet.getDuration() == 32767) {
                WynnTrans.incomeTextHandler.onSlownessApplied();
            }
        }
    }

    @Inject(method = "onRemoveEntityStatusEffect", at = @At("TAIL"))
    public void mixinOnRemoveEntityStatusEffect(RemoveEntityStatusEffectS2CPacket packet, CallbackInfo ci) {
        if(packet.getEntity(world) == MinecraftClient.getInstance().player) {
            if(packet.getEffectType() == StatusEffects.SLOWNESS) {
                WynnTrans.incomeTextHandler.onSlownessRemoved();
            }
        }
    }
}