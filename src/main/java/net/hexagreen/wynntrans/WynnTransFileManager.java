package net.hexagreen.wynntrans;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.mojang.logging.LogUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class WynnTransFileManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson gson = new Gson();
    private static final String fileName = "WynnTrans/scannedTexts.json";
    public static boolean addTranslation(String key, String value) {
        File file = new File(fileName);
        if(!file.exists()) return false;

        Map<String, String> target = new HashMap<>();
        target.put(key, value);
        String json = gson.toJson(target);
        String writeLine = ",\r\n\t" + json.replaceFirst("\\{", "");

        try(RandomAccessFile langFile = new RandomAccessFile(file, "rw")) {
            byte[] lineBytes = writeLine.getBytes(StandardCharsets.UTF_8);

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

    public static Set<String> readUnregistereds() {
        File file = new File(fileName);
        try {
            FileReader reader = new FileReader(file);
            Map<String, String> map = gson.fromJson(new JsonReader(reader), new TypeToken<HashMap<String, String>>() {}.getType());
            return map.keySet();
        } catch (FileNotFoundException e) {
            LOGGER.warn("[WynnTrans] Failed to read unregistered texts. File doesn't exist.");
        }
        return new HashSet<>();
    }
}
