package net.hexagreen.wynntrans.mixin;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Stream;

@Mixin(InGameHud.class)
abstract public class MixinInGameHud {
    @ModifyVariable(method = "setTitle", at = @At("HEAD"), argsOnly = true)
    public Text mixinSetTitle(Text text) {
        return WynnTrans.titleHandler.translateTitleText(text);
    }

    @ModifyVariable(method = "setSubtitle", at = @At("HEAD"), argsOnly = true)
    public Text mixinSetSubtitle(Text text) {
        return WynnTrans.titleHandler.translateSubtitleText(text);
    }

    @Redirect(method = "renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;limit(J)Ljava/util/stream/Stream;"))
    public Stream<ScoreboardEntry> mixinRenderScoreboardSidebar(Stream<ScoreboardEntry> stream, long l) {
        List<ScoreboardEntry> limited = stream.limit(l).toList();
        return WynnTrans.scoreboardSidebarHandler.translateScoreboardSidebar(limited).stream();
    }
}
