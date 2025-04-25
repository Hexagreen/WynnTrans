package net.hexagreen.wynntrans.enums;

import net.hexagreen.wynntrans.WynnTrans;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Locale;

public enum Identifications {
    STRENGTH("strength", "wytr.skill.strength", "Strength"),
    DEXTERITY("dexterity", "wytr.skill.dexterity", "Dexterity"),
    INTELLIGENCE("intelligence", "wytr.skill.intelligence", "Intelligence"),
    DEFENCE("defence", "wytr.skill.defence", "Defence"),
    AGILITY("agility", "wytr.skill.agility", "Agility"),
    NO_TYPE(null, null, null),
    UNKNOWN_MAJOR(null, null, null, true);

    private final String matches;
    private final String key;
    private final String val;
    private final boolean major;
    private String testInput;
    private String testInputSub;

    public static Identifications findIdentification(String string) {
        return Arrays.stream(Identifications.values())
                .filter(ids -> !ids.major)
                .filter(ids -> ids.isMatch(string))
                .findFirst()
                .orElse(NO_TYPE);
    }

    public static Identifications findMajorID(String idName, String idDesc) {
        return Arrays.stream(Identifications.values())
                .filter(ids -> ids.major)
                .filter(ids -> ids.isMatch(idName, idDesc))
                .findFirst()
                .orElse(UNKNOWN_MAJOR);
    }

    Identifications(String matches, String key, String val) {
        this.matches = matches;
        this.key = key;
        this.val = val;
        this.major = false;
    }

    Identifications(String matches, String key, String val, boolean major) {
        this.matches = matches;
        this.key = key;
        this.val = val;
        this.major = major;
    }

    public MutableText getTranslatedText() {
        String k = getKey();
        String v = getVal();
        if(WynnTrans.wynnTranslationStorage.checkTranslationExist(k, v)) {
            return Text.translatable(k);
        }
        return Text.literal(v);
    }

    public MutableText getMajorDesc() {
        String k = getKey() + ".desc";
        String v = testInputSub;
        if(WynnTrans.wynnTranslationStorage.checkTranslationExist(k, v)) {
            return Text.translatable(k);
        }
        return Text.literal(v);
    }

    private String getKey() {
        if(!this.major) {
            return switch(this) {
                case STRENGTH, DEXTERITY, INTELLIGENCE, DEFENCE, AGILITY -> this.key;
                case NO_TYPE -> "wytr.id." + testInput.replaceAll("[^0-9A-z]", "");
                default -> "wytr.id." + key;
            };
        }
        else {
            if(this == UNKNOWN_MAJOR) return "wytr.idMajor." + testInput.replaceAll("[^A-z]", "");
            else return "wytr.idMajor." + key;
        }
    }

    private String getVal() {
        if(this == NO_TYPE) return testInput.replaceAll("^ +| +$", "");
        if(this == UNKNOWN_MAJOR) return testInput.replaceAll("^\\+|: $", "");
        return val;
    }

    private boolean isMatch(String string) {
        if(this == NO_TYPE) {
            this.testInput = string;
            return false;
        }
        return this.matches.equals(string.toLowerCase(Locale.ENGLISH).replaceAll(" ", ""));
    }

    private boolean isMatch(String idName, String idDesc) {
        if(this == UNKNOWN_MAJOR) {
            this.testInput = idName;
            this.testInputSub = idDesc;
            return false;
        }
        return this.matches.equals(idName.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z]", ""));
    }
}
