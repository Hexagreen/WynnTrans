package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.debugClass;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MessageHandler.class)
abstract public class MixinMessageHandler {

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void mixinOnGameMessage(Text text, boolean bl, CallbackInfo ci) {
        if(text != null && !bl) {
            debugClass.writeString2File(text.getString(), "getString.txt");
            debugClass.writeString2File(text.toString(), "toString.txt");
            boolean confirm = WynnTrans.incomeTextHandler.sortIncomeText(text);
            if(confirm) ci.cancel();
        }
    }
}