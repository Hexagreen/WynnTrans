package net.hexagreen.wynntrans;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;

import static net.minecraft.server.command.CommandManager.literal;

public class WynnTrans implements ModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static IncomeTextHandler incomeTextHandler;
    public static WynnTranslationStorage wynnTranslationStorage;
    private static Iterator<String> debugString;

    @Override
    public void onInitialize() {
        incomeTextHandler = new IncomeTextHandler();
        wynnTranslationStorage = new WynnTranslationStorage();
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

        ClientTickEvents.START_WORLD_TICK.register((StartTick) -> incomeTextHandler.sendPendingText());
    }
}
