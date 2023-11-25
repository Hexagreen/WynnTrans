package net.hexagreen.wynntrans.mixin;

import net.minecraft.block.entity.SignText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(SignText.class)
abstract public class MixinSignText {
    @ModifyVariable(method = "create", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private static Text[] mixinCreate(Text[] messages) {
//        if(!Arrays.equals(messages, new Text[]{
//                ScreenTexts.EMPTY, ScreenTexts.EMPTY, ScreenTexts.EMPTY, ScreenTexts.EMPTY})) {
//            return WynnSign.get(messages).translate();
//        }
        return messages;
    }
}