package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.sign.WynnSign;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntity.class)
abstract public class MixinSignBlockEntity {
    @Shadow private SignText frontText;

    @Inject(method = "readNbt", at = @At("RETURN"))
    public void mixinReadNbt(NbtCompound nbt, CallbackInfo ci) {
        BlockPos pos = ((SignBlockEntity)(Object)this).getPos();
        if(pos.getY() > 10) {
            frontText = new SignText(WynnSign.get(frontText.getMessages(false)).translate(), frontText.getMessages(true), frontText.getColor(), frontText.isGlowing());
        }
    }
}
