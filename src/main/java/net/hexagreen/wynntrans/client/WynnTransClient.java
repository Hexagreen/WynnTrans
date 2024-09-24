package net.hexagreen.wynntrans.client;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class WynnTransClient implements ClientModInitializer {

	private static final Logger LOGGER = LogUtils.getLogger();

	/**
	 * Runs the mod initializer on the client environment.
	 */
	@Override
	public void onInitializeClient() {
		Path wynnTrans = FabricLoader.getInstance().getGameDir().resolve("WynnTrans");
		File dir = wynnTrans.toFile();
		try {
			if(!dir.exists()) {
				if(!dir.mkdirs()) throw new IOException();
			}
			File file = new File(dir, "scannedTexts.json");
			if(file.createNewFile()) {
				FileWriter writer = new FileWriter(file);
				writer.write("{\r\n\t\"_comment\":\"Scanned Texts Here\"}");
				writer.close();
			}
			File json = new File(dir, "json.txt");
			File excp = new File(dir, "exception.txt");
			if(json.createNewFile() && excp.createNewFile()) {
				LOGGER.info("[WynnTrans] Text capturing file initialized.");
			}
		} catch(IOException e) {
			LOGGER.warn("[WynnTrans] Cannot reach to WynnTrans directory or its file.");
		}
		LOGGER.info("[WynnTrans] Loaded.");
	}
}
