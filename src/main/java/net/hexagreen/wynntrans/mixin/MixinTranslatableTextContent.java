package net.hexagreen.wynntrans.mixin;

import net.minecraft.text.TranslatableTextContent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(TranslatableTextContent.class)
abstract public class MixinTranslatableTextContent {
    @ModifyVariable(method = "forEachPart", at = @At(value = "HEAD"), argsOnly = true)
    private String modifyForEachPart(String translation) {
        return translation.replaceAll("%%", "%");
    }

    @Redirect(method = "forEachPart", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", ordinal = 0))
    private int redirectIndexOf0(String instance, int ch) {
        return -1;
    }

    @Redirect(method = "forEachPart", at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", ordinal = 1))
    private int redirectIndexOf1(String instance, int ch) {
        return -1;
    }

    @Redirect(method = "forEachPart", at = @At(value = "INVOKE", target = "Ljava/util/regex/Pattern;matcher(Ljava/lang/CharSequence;)Ljava/util/regex/Matcher;"))
    private Matcher redirectMatcher(Pattern instance, CharSequence input) {
        return Pattern.compile("%(?:(\\d+)\\$)?(s)").matcher(input);
    }
}
