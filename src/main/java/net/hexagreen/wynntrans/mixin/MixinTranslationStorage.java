package net.hexagreen.wynntrans.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

@Mixin(TranslationStorage.class)
public abstract class MixinTranslationStorage {
    @Inject(method = "load(Lnet/minecraft/resource/ResourceManager;Ljava/util/List;Z)Lnet/minecraft/client/resource/language/TranslationStorage;", at = @At("RETURN"))
    private static void mixinLoad(ResourceManager resourceManager, List<String> definitions, boolean rightToLeft, CallbackInfoReturnable<TranslationStorage> cir, @Local Map<String, String> map) {
        if(definitions.getFirst().equals("en_us")) {
            WynnTrans.wynnTranslationStorage.onLanguageReloaded(map);
        }
    }
}