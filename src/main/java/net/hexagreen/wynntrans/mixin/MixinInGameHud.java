package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(InGameHud.class)
abstract public class MixinInGameHud {
    @ModifyVariable(method = "setTitle", at = @At("HEAD"), argsOnly = true)
    public Text mixinSetTitle(Text text) {
        return WynnTrans.titleHandler.translateTitleText(text);
    }

    @ModifyVariable(method = "setSubtitle", at = @At("HEAD"), argsOnly = true)
    public Text mixinSetSubtitle(Text text) {
        return WynnTrans.titleHandler.translateSubtitleText(text);
    }
}
