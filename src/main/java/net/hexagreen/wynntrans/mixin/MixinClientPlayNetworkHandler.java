package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.entity.living.OnEntityTrackerUpdateHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
abstract public class MixinClientPlayNetworkHandler {
    @Inject(method = "onEntityTrackerUpdate(Lnet/minecraft/network/packet/s2c/play/EntityTrackerUpdateS2CPacket;)V", at=@At("TAIL"))
    public void mixinOnEntityTrackerUpdate(EntityTrackerUpdateS2CPacket packet, CallbackInfo ci) {
        if(!MinecraftClient.getInstance().isOnThread()) return;
        new OnEntityTrackerUpdateHandler(packet);
    }

    @Redirect(method = "onEntityPassengersSet", at= @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;)V"))
    public void mixinWarn(Logger instance, String s) {
        //noinspection UnnecessaryReturnStatement
        return;
    }

    @Redirect(method = "onPlayerList", at= @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
    public void mixinWarn(Logger instance, String s, Object o0, Object o1) {
        //noinspection UnnecessaryReturnStatement
        return;
    }
}
