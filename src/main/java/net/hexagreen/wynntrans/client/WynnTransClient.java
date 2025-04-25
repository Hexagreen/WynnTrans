package net.hexagreen.wynntrans.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.loader.api.FabricLoader;
import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class WynnTransClient implements ClientModInitializer {
    private static final Logger LOGGER = WynnTrans.LOGGER;
    private static KeyBinding showOriginalTooltipKey;

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        readyForWynnTransStorage();

        showOriginalTooltipKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "wytr.key.showOriginalTooltip",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_KP_SUBTRACT,
                "wytr.wynnTrans"
        ));

        KeyPressEvent.EVENT.register((keyCode, scanCode, modifiers) -> {
            if(keyCode == KeyBindingHelper.getBoundKeyOf(showOriginalTooltipKey).getCode()) {
                WynnTrans.drawTooltipHandler.toggleShowOriginal();
            }
        });
    }

    private void readyForWynnTransStorage() {
        Path wynnTrans = FabricLoader.getInstance().getGameDir().resolve("WynnTrans");
        File dir = wynnTrans.toFile();
        try {
            if(!dir.exists()) {
                if(!dir.mkdirs()) throw new IOException();
            }
            File scanned = new File(dir, "scannedTexts.json");
            if(scanned.createNewFile()) {
                FileWriter writer = new FileWriter(scanned);
                writer.write("{\r\n\t\"_comment\":\"Scanned Texts Here\"}");
                writer.close();
            }
            File json = new File(dir, "json.txt");
            if(json.createNewFile()) {
                LOGGER.info("Text capturing file initialized.");
            }
        }
        catch(IOException e) {
            LOGGER.warn("Cannot reach to WynnTrans directory or its file.");
        }
        LOGGER.info("Loaded.");
    }

    public interface KeyPressEvent {
        Event<KeyPressEvent> EVENT = EventFactory.createArrayBacked(KeyPressEvent.class,
                (listeners) -> (keyCode, scanCode, modifiers) -> {
                    for(KeyPressEvent listener : listeners) {
                        listener.onKeyPress(keyCode, scanCode, modifiers);
                    }
                });

        void onKeyPress(int keyCode, int scanCode, int modifiers);
    }
}
