package net.hexagreen.wynntrans.enums;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Objectives {
    SLAY_LEVELED_MOBS(Pattern.compile("Slay Lv\\. (\\d+)\\+ Mobs"), "SlayLeveledMobs", "Slay Lv. %s+ Mobs"),
    TIERED_LOOT_CHEST(Pattern.compile("Loot Chests T(\\d)\\+"), "LootChestsTiered", "Loot Chests T%s+"),
    NO_TYPE(null, null, null);

    private final Pattern regex;

    public String getNormalizedKey() {
        return normalizedKey;
    }

    public String getNormalizedVal() {
        return normalizedVal;
    }

    public Object getNormalizedArg() {
        return normalizedArg;
    }

    private final String normalizedKey;
    private final String normalizedVal;
    private Object normalizedArg;

    Objectives(Pattern regex, String normalizedKey, String normalizedVal) {
        this.regex = regex;
        this.normalizedKey = normalizedKey;
        this.normalizedVal = normalizedVal;
        this.normalizedArg = null;
    }

    boolean isMatch(String string) {
        if (this == NO_TYPE) return false;
        Matcher m = this.regex.matcher(string);
        boolean result = m.find();
        if (result) {
            normalizedArg = m.group(1);
            return true;
        }
        return false;
    }

    public static Objectives findNormalized(String string) {
        return Arrays.stream(Objectives.values())
                .filter(objectives -> objectives.isMatch(string))
                .findFirst().orElse(NO_TYPE);
    }
}
