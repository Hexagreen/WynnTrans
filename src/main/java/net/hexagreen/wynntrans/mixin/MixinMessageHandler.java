package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = MessageHandler.class, priority = 900)
abstract public class MixinMessageHandler {
    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void mixinOnGameMessage(Text text, boolean bl, CallbackInfo ci) {
        if(text != null && !bl) {
            boolean confirm = WynnTrans.incomeTextHandler.sortIncomeText(text);
            if(confirm) ci.cancel();
        }
    }
}