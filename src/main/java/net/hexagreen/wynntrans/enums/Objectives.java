package net.hexagreen.wynntrans.enums;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Objectives {
    TIERED_LOOT_CHEST(Pattern.compile("Loot Chests T(\\d)\\+"), "LootChestsTiered", "Loot Chests T%s+"),
    SLAY_LEVELED_MOBS(Pattern.compile("Slay Lv\\. (\\d+)\\+ Mobs"), "SlayLeveledMobs", "Slay Lv. %s+ Mobs"),
    NO_TYPE(null, null, null);

    private final Pattern regex;
    private final String normalizedKey;
    private final String normalizedVal;
    private Object[] normalizedArg;
    private String testInput;

    public static Objectives findNormalized(String string) {
        return Arrays.stream(Objectives.values())
                .filter(objectives -> objectives.isMatch(string))
                .findFirst()
                .orElse(NO_TYPE);
    }

    Objectives(Pattern regex, String normalizedKey, String normalizedVal) {
        this.regex = regex;
        this.normalizedKey = normalizedKey;
        this.normalizedVal = normalizedVal;
        this.normalizedArg = new Object[0];
    }

    public String getNormalizedKey() {
        if(this == NO_TYPE) {
            return "wytr.objective." + testInput.replace(" ", "");
        }
        return "wytr.objective." + normalizedKey;
    }

    public String getNormalizedVal() {
        if(this == NO_TYPE) return testInput;
        return normalizedVal;
    }

    public Object[] getNormalizedArg() {
        return normalizedArg;
    }

    public MutableText getTranslatedText() {
        String k = getNormalizedKey();
        String v = getNormalizedVal();
        Object[] a = getNormalizedArg();
        if(WynnTrans.wynnTranslationStorage.checkTranslationExist(k, v)) {
            return Text.translatable(k, a);
        }
        return Text.literal(v);
    }

    boolean isMatch(String string) {
        if(this == NO_TYPE) {
            this.testInput = string;
            return false;
        }
        Matcher m = this.regex.matcher(string);
        List<String> args = new ArrayList<>();
        if(m.find()) {
            int groups = m.groupCount();
            for(int i = 0; i < groups; i++) {
                args.add(m.group(1));
            }
            this.normalizedArg = args.toArray(Object[]::new);
            return true;
        }
        return false;
    }
}
