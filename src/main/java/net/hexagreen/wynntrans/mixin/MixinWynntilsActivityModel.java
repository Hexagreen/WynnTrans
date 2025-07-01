package net.hexagreen.wynntrans.mixin;

import com.wynntils.core.text.StyledText;
import com.wynntils.models.activities.ActivityModel;
import net.hexagreen.wynntrans.text.WynntilsCompatible;
import net.hexagreen.wynntrans.text.tooltip.types.ContentBookNodes;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ActivityModel.class)
public abstract class MixinWynntilsActivityModel {
    @Unique
    private static final Object[] collectorInstance = new Object[1];

    @Inject(method = "getTrackedName", at = @At(value = "RETURN"), cancellable = true, remap = false)
    private void getTrackedNameMixin(CallbackInfoReturnable<String> cir) {
        String original = cir.getReturnValue();
        if(original.isEmpty()) {
            cir.setReturnValue("");
            return;
        }
        List<Text> dummy = new ArrayList<>();
        dummy.add(Text.of("dummy"));
        ContentBookNodes contentBookNodes = new ContentBookNodes(dummy);
        cir.setReturnValue(contentBookNodes.processWynntilsActivityModelName(collectorInstance, original));
    }

    @Inject(method = "getTrackedTask", at = @At(value = "RETURN"), cancellable = true, remap = false)
    private void getTrackedTaskMixin(CallbackInfoReturnable<StyledText> cir) {
        StyledText original = cir.getReturnValue();
        MutableText text = WynntilsCompatible.getTextFromWynntilsText(original);
        if(text.getString().isEmpty()) {
            cir.setReturnValue(original);
            return;
        }
        List<Text> dummy = new ArrayList<>();
        dummy.add(Text.of("dummy"));
        ContentBookNodes contentBookNodes = new ContentBookNodes(dummy);
        Text modified = contentBookNodes.processWynntilsActivityModelDesc(collectorInstance, text);
        cir.setReturnValue(WynntilsCompatible.getWynntilsTextFromText(modified));
    }
}
