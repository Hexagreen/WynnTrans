package net.hexagreen.wynntrans;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class debugClass {
    private static final String path = FabricLoader.getInstance().getGameDir().resolve("WynnTrans") + File.separator;

    public static void writeString2File(String str, String fileName) {
        try{
        FileWriter fw = new FileWriter(path + fileName, true);
        fw.write(str);
        fw.write("\n~~~~~\n");
        fw.close();
        } catch(IOException ignored) {}
    }

    public static void writeString2File(String str, String fileName, String tag) {
        writeString2File("[" + tag + "] | " + str, fileName);
    }

    public static void writePacket(PacketByteBuf buf) {
        if(buf.readText() != null) {
            debugClass.writeString2File(buf.readString(), "packet.txt");
        }
    }

    public static void writeTextList(List<Text> text, String fileName) {
        if(text != null) {
            debugClass.writeString2File(assembleTextFromList(text).getString(), fileName);
        }
    }

    public static void writeTextAsJSON(Text text) {
        String str = Text.Serializer.toJson(text);
        debugClass.writeString2File(str, "json.txt");
    }

    public static Text assembleTextFromList(List<Text> texts) {
        MutableText text = Text.empty();
        for(Text t : texts) {
            text.append(t);
        }

        return text;
    }

    public static String[] readTextListFromJSON() {
        try{
            String str = Files.readString(Paths.get("D:/debug/json.txt"));
            return str.split("\r\n~~~~~\r\n");
        } catch (Exception ignored) {}
        return new String[]{"EMPTY"};
    }
}
