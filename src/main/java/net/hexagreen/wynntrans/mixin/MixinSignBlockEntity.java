package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.hexagreen.wynntrans.sign.WynnSign;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.SignText;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SignBlockEntity.class)
abstract public class MixinSignBlockEntity {
    @Inject(method = "readNbt", at = @At("HEAD"))
    public void mixinReadNbt(NbtCompound nbt, CallbackInfo ci) {
        BlockPos pos = ((SignBlockEntity)(Object)this).getPos();
        if(pos.getY() > 15) {
            WynnTrans.translationTargetSignMarker = true;
        }
    }
}
