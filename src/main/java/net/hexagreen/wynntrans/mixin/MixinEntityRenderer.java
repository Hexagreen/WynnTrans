package net.hexagreen.wynntrans.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
abstract public class MixinEntityRenderer {
    @ModifyVariable(method = "renderLabelIfPresent", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    protected Text mixinRenderLabelIfPresent(Text text, @Local(argsOnly = true) Entity entity) {
        if(!(entity instanceof PlayerEntity)) {
            if(text.getString().matches("§f.+§7's horse")) {
                return Text.translatable("wytr.label.horse", text.getString().replaceFirst("§7's.+", "")).setStyle(Style.EMPTY.withColor(Formatting.GRAY));
            }

            String valContent = text.getString();
            String keyContent = "wytr.label." + DigestUtils.sha1Hex(valContent);
            if(WynnTrans.wynnTranslationStorage.checkTranslationExist(keyContent, valContent)) {
                return Text.translatable(keyContent).setStyle(text.getStyle());
            }
        }
        return text;
    }
}
