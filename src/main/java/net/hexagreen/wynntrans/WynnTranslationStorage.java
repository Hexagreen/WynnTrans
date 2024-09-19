package net.hexagreen.wynntrans;

import com.mojang.logging.LogUtils;
import net.minecraft.text.Text;
import org.slf4j.Logger;

import java.util.*;

public class WynnTranslationStorage {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<String, String> wynnTransDict = new HashMap<>();
    private final Set<String> unregisteredTextSet = new HashSet<>();
    private final Set<List<Text>> unregisteredTooltipSet = new HashSet<>();
    private final Set<Text> unregisteredEntityLabelSet = new HashSet<>();

    public void onLanguageReloaded(Map<String, String> translationMap) {
        wynnTransDict.clear();
        unregisteredTextSet.clear();

        wynnTransDict.putAll(translationMap);
        unregisteredTextSet.addAll(WynnTransFileManager.readUnregistered());
        LOGGER.info("[WynnTrans] Reloaded Wynncraft Text Language Map.");
    }

    public void recordUnregisteredTooltip(List<Text> text, String tag) {
        if(this.unregisteredTooltipSet.contains(text)) {
            return;
        }
        this.unregisteredTooltipSet.add(text);
        debugClass.writeString2File("", "json.txt");
        for(Text line : text) debugClass.writeTextAsJSON(line, tag);
        debugClass.writeString2File("", "json.txt");
    }

    public void recordUnregisteredEntity(Text text, String tag) {
        if(this.unregisteredEntityLabelSet.contains(text)) {
            return;
        }
        this.unregisteredEntityLabelSet.add(text);
        debugClass.writeTextAsJSON(text, tag);
    }

    public boolean checkTranslationExist(String key, String value) {
        String v = this.wynnTransDict.get(key);
        if(v == null) {
            this.recordUnregisteredText(key, value);
            return false;
        }
        return true;
    }

    public boolean checkTranslationDoNotRegister(String key) {
        String v = this.wynnTransDict.get(key);
        return !(v == null);
    }

    private void recordUnregisteredText(String key, String value) {
        if(this.unregisteredTextSet.contains(key)) {
            return;
        }
        this.unregisteredTextSet.add(key);
        boolean recorded = WynnTransFileManager.addTranslation(key, value);
        if(!recorded) {
            LOGGER.warn("[WynnTrans] Failed to record translation.");
            LOGGER.warn("[WynnTrans] key: {}", key);
            LOGGER.warn("[WynnTrans] value: {}", value);
        }
    }
}
