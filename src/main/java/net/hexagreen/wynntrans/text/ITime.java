package net.hexagreen.wynntrans.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ITime {
    Pattern timePattern = Pattern.compile("(?:(/|\\d+(?:\\.\\d+)?)(?:days|day|d))?(?:(/|\\d+(?:\\.\\d+)?)(?:hours|hour|h))?(?:(/|\\d+(?:\\.\\d+)?)(?:minutes|minute|min|m))?(?:(/|\\d+(?:\\.\\d+)?)(?:seconds|second|sec|s))?");

    static MutableText translateTime(Text text) {
        return translateTime(text.getString()).setStyle(text.getStyle());
    }

    static MutableText translateTime(String string) {
        Matcher matcher = timePattern.matcher(string.replaceAll(" ", "").replaceAll("§.", ""));
        MutableText result = Text.empty();
        if(matcher.find()) {
            String d = matcher.group(1), h = matcher.group(2), m = matcher.group(3), s = matcher.group(4);
            if(d != null) {
                result.append(Text.translatable("wytr.time.day", d));
                if(hasBack(h, m, s)) result.append(" ");
            }
            if(h != null) {
                result.append(Text.translatable("wytr.time.hour", h));
                if(hasBack(m, s)) result.append(" ");
            }
            if(m != null) {
                result.append(Text.translatable("wytr.time.minute", m));
                if(hasBack(s)) result.append(" ");
            }
            if(s != null) {
                result.append(Text.translatable("wytr.time.second", s));
            }
        }
        return result;
    }

    static boolean hasBack(String... strings) {
        for(String str : strings) {
            if(str != null) return true;
        }
        return false;
    }
}
