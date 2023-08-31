package net.hexagreen.wynntrans.client;

import net.fabricmc.api.ClientModInitializer;

public class WynnTransClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        System.out.println("[WynnTrans] Loaded.");
    }
}
