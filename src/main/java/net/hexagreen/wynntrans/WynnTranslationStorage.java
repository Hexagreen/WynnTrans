package net.hexagreen.wynntrans;

import net.minecraft.client.resource.language.TranslationStorage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WynnTranslationStorage {
    private Map<String, String> wynnTransDict = new HashMap<>();
    private Set<String> unregisteredTextSet = new HashSet<>();
    private TranslationStorage wynnTranslationStorage;

    public WynnTranslationStorage() {
        this.wynnTranslationStorage = null;
    }

    public void setWynnTranslationStorage(TranslationStorage trs) {
        this.wynnTranslationStorage = trs;
    }

    public void onLanguageReloaded(Map<String, String> translationMap) {
        wynnTransDict.clear();
        unregisteredTextSet.clear();

        wynnTransDict.putAll(translationMap);
        System.out.println("[WynnTrans] Reloaded Wynncraft Text Language Map.");
    }

    public boolean checkTranslationExist(String key, String value) {
        String v = this.wynnTransDict.get(key);
        if(v == null) {
            this.recordUnregisteredText(key, value);
            return false;
        }
        return true;
    }

    private void recordUnregisteredText(String key, String value) {
        if(this.unregisteredTextSet.contains(key)) {
            return;
        }
        this.unregisteredTextSet.add(key);
        boolean recorded = ResourcepackGenerator.addTranslation(key, value);
        if(!recorded) System.out.println("[WynnTrans] Failed to record translation key: " + key);
    }
}
