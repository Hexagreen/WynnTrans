package net.hexagreen.wynntrans;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.hexagreen.wynntrans.text.chat.OnGameMessageHandler;
import net.hexagreen.wynntrans.text.display.DisplayEntityHandler;
import net.hexagreen.wynntrans.text.scoreboard.ScoreboardSidebarHandler;
import net.hexagreen.wynntrans.text.sign.UseBlockHandler;
import net.hexagreen.wynntrans.text.title.TitleHandler;
import net.hexagreen.wynntrans.text.tooltip.DrawTooltipHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WynnTrans implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("WynnTrans");
    public static WynnTranslationStorage wynnTranslationStorage;
    public static OnGameMessageHandler onGameMessageHandler;
    public static DisplayEntityHandler displayEntityHandler;
    public static DrawTooltipHandler drawTooltipHandler;
    public static TitleHandler titleHandler;
    public static ScoreboardSidebarHandler scoreboardSidebarHandler;
    public static boolean translationTargetSignMarker;
    public static boolean playerNameCacheExpired;
    public static String wynnPlayerName;

    public static void refreshWynnPlayerName() {
        WynnTransFileManager.whoAmI();
        playerNameCacheExpired = false;
    }

    public static void expireWynnPlayerName() {
        playerNameCacheExpired = true;
    }

    @Override
    public void onInitialize() {
        wynnTranslationStorage = new WynnTranslationStorage();
        onGameMessageHandler = new OnGameMessageHandler();
        displayEntityHandler = new DisplayEntityHandler();
        drawTooltipHandler = new DrawTooltipHandler();
        titleHandler = new TitleHandler();
        scoreboardSidebarHandler = new ScoreboardSidebarHandler();
        translationTargetSignMarker = false;
        playerNameCacheExpired = true;
        wynnPlayerName = "DummyEmptyPlayerName";

        LOGGER.info("Hello, Wynn!");

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandToggleBackgroundTextRegistration.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandToggleRecordMode.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandToggleDisplayTextRecordMode.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandToggleTooltipTextRecordMode.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> CommandToggleTitleTextRecordMode.register(dispatcher));

        ClientTickEvents.START_WORLD_TICK.register((StartTick) -> onGameMessageHandler.onStartWorldTick());

        UseBlockCallback.EVENT.register(new UseBlockHandler());
        ScreenEvents.AFTER_INIT.register((c, s, w, h) -> {
            if("\uDAFF\uDFD5\uE01F".equals(s.getTitle().getString())) expireWynnPlayerName();
        });
    }

    private static class CommandToggleRecordMode {
        private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(
                    ClientCommandManager.literal("wytr")
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
                    ClientCommandManager.literal("wytr")
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
                    ClientCommandManager.literal("wytr")
                            .then(ClientCommandManager.literal("recordDisplay")
                                    .executes(context -> run())));
        }

        private static int run() {
            displayEntityHandler.toggleRecordMode();
            return 1;
        }
    }

    private static class CommandToggleTooltipTextRecordMode {
        private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(
                    ClientCommandManager.literal("wytr")
                            .then(ClientCommandManager.literal("recordTooltip")
                                    .executes(context -> run())));
        }

        private static int run() {
            drawTooltipHandler.toggleRecordMode();
            return 1;
        }
    }

    private static class CommandToggleTitleTextRecordMode {
        private static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
            dispatcher.register(
                    ClientCommandManager.literal("wytr")
                            .then(ClientCommandManager.literal("recordTitle")
                                    .executes(context -> run())));
        }

        private static int run() {
            titleHandler.toggleRecordMode();
            return 1;
        }
    }
}
