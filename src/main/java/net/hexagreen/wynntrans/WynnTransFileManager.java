package net.hexagreen.wynntrans;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;


public class WynnTransFileManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    private static final String fileName = "WynnTrans/scannedTexts.json";
    public static boolean addTranslation(String key, String value) {
        Map<String, String> target = new HashMap<>();
        target.put(key, value);
        String json = gson.toJson(target);
        String writeLine = ",\r\n\t" + json.replaceFirst("\\{", "");
        return writeToFile(writeLine);
    }

    public static void addSpace(String string) {
        String str = "\r\n\t";
        if(!string.isEmpty()) {
            str = ",\r\n\t\"_c_" + string + "\":\"" + string + "\"";
        }
        str += "}";
        writeToFile(str);
    }

    public static Set<String> readUnregistered() {
        File file = new File(fileName);
        try(FileReader reader = new FileReader(file)) {
            Map<String, String> map = gson.fromJson(new JsonReader(reader), new TypeToken<HashMap<String, String>>() {}.getType());
            return map.keySet();
        } catch (IOException e) {
            LOGGER.warn("[WynnTrans] Failed to read unregistered texts. File doesn't exist.");
        } catch (JsonSyntaxException e) {
            String timestamp = new SimpleDateFormat("MMdd_HHmmss").format(new Date());
            try {
                Files.move(file, new File("WynnTrans/scannedTexts" + timestamp + ".json"));
                if(file.createNewFile()) {
                    FileWriter writer = new FileWriter(file);
                    writer.write("{\r\n\t\"_comment\":\"Scanned Texts Here\"}");
                    writer.close();
                }
            } catch (IOException ex) {
                LOGGER.error("[WynnTrans] Failed to backup and read unregistered texts. Restart game.");
                System.exit(1);
            }
        }
        return new HashSet<>();
    }

    private static boolean writeToFile(String line) {
        File file = new File(fileName);
        if(!file.exists()) return false;

        try(RandomAccessFile langFile = new RandomAccessFile(file, "rw")) {
            byte[] lineBytes = line.getBytes(StandardCharsets.UTF_8);

            int readLen = 1024;
            int from = (int)file.length() - readLen;
            byte[] buffer = new byte[1024];

            if(from < 0) {
                readLen += from;
                from = 0;
            }

            langFile.seek(from);
            langFile.readFully(buffer, 0, readLen);
            int lastBracket = ArrayUtils.lastIndexOf(buffer, (byte)'}');
            if(lastBracket < 0) throw new IOException();

            from += lastBracket;

            langFile.seek(from);
            langFile.write(lineBytes);

        } catch (IOException e) {
            LOGGER.warn("[WynnTrans] Failed to load language file. Is file corrupted?");
        }
        return true;
    }
}
