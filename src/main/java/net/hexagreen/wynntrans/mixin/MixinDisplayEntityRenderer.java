package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.render.entity.DisplayEntityRenderer;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(DisplayEntityRenderer.TextDisplayEntityRenderer.class)
abstract public class MixinDisplayEntityRenderer {

	@ModifyVariable(method = "getLines", at = @At("HEAD"), ordinal = 0, argsOnly = true)
	private Text mixinGetLines(Text text) {
		return WynnTrans.displayEntityHandler.translateDisplayText(text);
	}
}
