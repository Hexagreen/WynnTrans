package net.hexagreen.wynntrans.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.text.sign.WynnSign;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
abstract public class MixinEntityRenderer {
    @ModifyVariable(method = "renderLabelIfPresent", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    protected <S extends EntityRenderState> Text mixinRenderLabelIfPresent(Text text, @Local(argsOnly = true) S entityRenderState) {
        if(!(entityRenderState instanceof PlayerEntityRenderState)) {
            if(text.getString().matches("§f.+§7's horse")) {
                String owner = text.getString().replaceFirst("§7's.+", "");
                return Text.translatable("wytr.label.horse", owner).formatted(Formatting.GRAY);
            }
            if(text.getString().matches("(§.)?\\[?Lv\\. [\\d/]+]?")) {
                return WynnSign.translateRecommendLevel(text);
            }
            if(text.getString().replaceAll("§.", "").matches("\\W+")) {
                return text;
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
