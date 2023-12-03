package net.hexagreen.wynntrans;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.hexagreen.wynntrans.sign.UseBlockHandler;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

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
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("readJson")
                .executes(context -> {
                    if(debugString.hasNext()) {
                        Text readText = Text.Serializer.fromJson(debugString.next());
                        context.getSource().sendMessage(readText);
                    }
                    return 1;
                })));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> WriteCommentClass.register(dispatcher));

        ClientTickEvents.START_WORLD_TICK.register((StartTick) -> incomeTextHandler.onStartWorldTick());
        UseBlockCallback.EVENT.register(new UseBlockHandler());
    }
    private static class WriteCommentClass {
        private static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
            dispatcher.register(literal("wynntrans")
                    .then(literal("comment")
                            .then(argument("string", greedyString())
                                    .executes(context -> newComment(getString(context, "string"))))));
        }
        private static int newComment(String comment) {
            WynnTransFileManager.addSpace(comment);
            return 1;
        }
    }
}
