package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.entity.EntityTrackerUpdateHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
abstract public class MixinClientPlayNetworkHandler {
    @Inject(method = "onEntityTrackerUpdate(Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;)V", at=@At("HEAD"))
    public void mixinOnEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci){
        if(!MinecraftClient.getInstance().isOnThread()) return;
        new EntityTrackerUpdateHandler(packet);
    }
}
