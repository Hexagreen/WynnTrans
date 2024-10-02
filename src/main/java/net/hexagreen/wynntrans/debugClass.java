package net.hexagreen.wynntrans;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class debugClass {
    private static final String path = FabricLoader.getInstance().getGameDir().resolve("WynnTrans") + File.separator;

    public static void writeString2File(String str, String fileName) {
        try {
            FileWriter fw = new FileWriter(path + fileName, true);
            fw.write(str);
            fw.write("\n~~~~~\n");
            fw.close();
        }
        catch(IOException ignored) {
        }
    }

    public static void writeString2File(String str, String fileName, String tag) {
        writeString2File("[" + tag + "] | " + str, fileName);
    }

    public static void writeTextAsJSON(Text text, String tag) {
        @SuppressWarnings("DataFlowIssue") String str = Text.Serialization.toJsonString(text, MinecraftClient.getInstance().world.getRegistryManager());
        debugClass.writeString2File(str, "json.txt", tag);
    }

    public static void writeTextAsJSON(Text text) {
        debugClass.writeTextAsJSON(text, "");
    }

    public static String[] readTextListFromJSON() {
        try {
            String str = Files.readString(Paths.get("D:/debug/json.txt"));
            return str.split("\r\n~~~~~\r\n");
        }
        catch(Exception ignored) {
        }
        return new String[]{"EMPTY"};
    }
}
