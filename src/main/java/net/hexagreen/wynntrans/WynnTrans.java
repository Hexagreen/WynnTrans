package net.hexagreen.wynntrans;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.hexagreen.wynntrans.text.chat.OnGameMessageHandler;
import net.hexagreen.wynntrans.text.display.DisplayEntityHandler;
import net.hexagreen.wynntrans.text.sign.UseBlockHandler;
import net.hexagreen.wynntrans.text.tooltip.DrawTooltipHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;

public class WynnTrans implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static OnGameMessageHandler onGameMessageHandler;
    public static DisplayEntityHandler displayEntityHandler;
    public static DrawTooltipHandler drawTooltipHandler;
    public static WynnTranslationStorage wynnTranslationStorage;
    public static boolean translationTargetSignMarker;
    private static Iterator<String> debugString;

    @Override
    public void onInitialize() {
        onGameMessageHandler = new OnGameMessageHandler();
        displayEntityHandler = new DisplayEntityHandler();
        drawTooltipHandler = new DrawTooltipHandler();
        wynnTranslationStorage = new WynnTranslationStorage();
        translationTargetSignMarker = false;

        LOGGER.info("[WynnTrans] Hello, Wynn!");

        debugString = List.of(debugClass.readTextListFromJSON()).iterator();

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> CommandReadJson.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandWriteComment.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandToggleRecordMode.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandToggleBackgroundTextRegistration.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandToggleDisplayTextRecordMode.register(dispatcher));

        ClientTickEvents.START_WORLD_TICK.register((StartTick) -> onGameMessageHandler.onStartWorldTick());

        UseBlockCallback.EVENT.register(new UseBlockHandler());
    }

    private static class CommandReadJson {
        private static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
            dispatcher.register(CommandManager.literal("readJson").executes(CommandReadJson::run));
        }

        private static int run(CommandContext<ServerCommandSource> context) {
            if(debugString.hasNext()) {
                @SuppressWarnings("DataFlowIssue") Text readText = Text.Serialization.fromJson(debugString.next(), MinecraftClient.getInstance().world.getRegistryManager());
                context.getSource().sendMessage(readText);
            }
            return 1;
        }
    }

    private static class CommandWriteComment {
        private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(
                    ClientCommandManager.literal("wynntrans")
                            .then(ClientCommandManager.literal("comment")
                                    .executes(context -> run())));
        }

        private static int run() {
            WynnTransFileManager.addSpace("");
            return 1;
        }
    }

    private static class CommandToggleRecordMode {
        private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(
                    ClientCommandManager.literal("wynntrans")
                            .then(ClientCommandManager.literal("recordMode")
                                    .executes(context -> run())));
        }

        private static int run() {
            onGameMessageHandler.toggleRecordMode();
            return 1;
        }
    }

    private static class CommandToggleBackgroundTextRegistration {
        private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(
                    ClientCommandManager.literal("wynntrans")
                            .then(ClientCommandManager.literal("registerBT")
                                    .executes(context -> run())));
        }

        private static int run() {
            onGameMessageHandler.toggleBTRegisterMode();
            return 1;
        }
    }

    private static class CommandToggleDisplayTextRecordMode {
        private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(
                    ClientCommandManager.literal("wynntrans")
                            .then(ClientCommandManager.literal("recordDisplay")
                                    .executes(context -> run())));
        }

        private static int run() {
            displayEntityHandler.toggleRecordMode();
            return 1;
        }
    }
}
