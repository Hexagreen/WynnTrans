package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPlayNetworkHandler.class, priority = 900)
abstract public class MixinClientPlayNetworkHandler {
    @Redirect(method = "onEntityPassengersSet", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;)V"))
    public void mixinWarn(Logger instance, String s) {
    }

    @Redirect(method = "onPlayerList", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"))
    public void mixinWarn(Logger instance, String s, Object o0, Object o1) {
    }

    @Inject(method = "onGameMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/message/MessageHandler;onGameMessage(Lnet/minecraft/text/Text;Z)V"), cancellable = true)
    public void mixinOnGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        if(packet.content() != null && !packet.overlay()) {
            boolean confirm = WynnTrans.onGameMessageHandler.translateChatText(packet.content());
            if(confirm) ci.cancel();
        }
    }
}
