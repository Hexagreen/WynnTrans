package net.hexagreen.wynntrans.mixin;

import net.minecraft.client.network.message.MessageHandler;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = MessageHandler.class, priority = 900)
abstract public class MixinMessageHandler {
//    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
//    public void mixinOnGameMessage(Text text, boolean bl, CallbackInfo ci) {
//        if(text != null && !bl) {
//            boolean confirm = WynnTrans.onGameMessageHandler.translateChatText(text);
//            if(confirm) ci.cancel();
//        }
//    }
}