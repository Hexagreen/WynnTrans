package net.hexagreen.wynntrans;

import com.google.gson.Gson;
import org.apache.commons.lang3.ArrayUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ResourcepackGenerator {
    private static final Gson gson = new Gson();
    public static boolean addTranslation(String key, String value) {
        String fileName = "WynnTrans/scannedTexts.json";
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
            System.out.println("[WynnTrans] Failed to load language file. Is file corrupted?");
        }
        return true;
    }
}
