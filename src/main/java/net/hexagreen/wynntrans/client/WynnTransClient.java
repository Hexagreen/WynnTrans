package net.hexagreen.wynntrans.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;

public class WynnTransClient implements ClientModInitializer {
    private static final Logger LOGGER = LogUtils.getLogger();
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        LOGGER.info("[WynnTrans] Loaded.");
    }
}
