package net.hexagreen.wynntrans;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.hexagreen.wynntrans.sign.UseBlockHandler;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;

public class WynnTrans implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static IncomeTextHandler incomeTextHandler;
    public static WynnTranslationStorage wynnTranslationStorage;
    public static boolean translationTargetSignMarker;
    private static Iterator<String> debugString;

    @Override
    public void onInitialize() {
        incomeTextHandler = new IncomeTextHandler();
        wynnTranslationStorage = new WynnTranslationStorage();
        translationTargetSignMarker = false;

        LOGGER.info("[WynnTrans] Hello, Wynn!");

        debugString = List.of(debugClass.readTextListFromJSON()).iterator();
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(CommandManager.literal("readJson")
                .executes(context -> {
                    if(debugString.hasNext()) {
                        Text readText = Text.Serializer.fromJson(debugString.next());
                        context.getSource().sendMessage(readText);
                    }
                    return 1;
                })));

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> WriteCommentClass.register(dispatcher));

        ClientTickEvents.START_WORLD_TICK.register((StartTick) -> incomeTextHandler.onStartWorldTick());
        UseBlockCallback.EVENT.register(new UseBlockHandler());
    }
    private static class WriteCommentClass {
        private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(ClientCommandManager.literal("wynntrans")
                    .then(ClientCommandManager.literal("comment")
                            .executes(context -> newComment())));
        }

        private static int newComment() {
            WynnTransFileManager.addSpace("");
            return 1;
        }
    }
}
