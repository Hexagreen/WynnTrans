package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.resource.language.TranslationStorage;
import net.minecraft.resource.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LanguageManager.class)
public abstract class MixinLanguageManager {

    @Inject(method = "reload", at = @At("TAIL"))
    public void mixinReload(ResourceManager manager, CallbackInfo ci) {
        TranslationStorage trs = TranslationStorage.load(manager, List.of("wy_tr"), false);
        WynnTrans.wynnTranslationStorage.setWynnTranslationStorage(trs);
    }
}
