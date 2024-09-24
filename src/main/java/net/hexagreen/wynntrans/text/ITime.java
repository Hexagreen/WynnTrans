package net.hexagreen.wynntrans.text;

import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface ITime {

	Pattern timePattern = Pattern.compile("(?:(\\d+)(?:hours|hour|h))?(?:(\\d+)(?:minutes|minute|min|m))?(?:(\\d+)(?:seconds|second|sec|s))?(?:(\\d+)(?:days|day|d))?");

	static MutableText translateTime(String string) {
		Matcher matcher = timePattern.matcher(string.replaceAll(" ", ""));
		MutableText result = Text.empty();
		if(matcher.find()) {
			String h = matcher.group(1), m = matcher.group(2), s = matcher.group(3), d = matcher.group(4);
			if(h != null) {
				result.append(Text.translatable("wytr.time.hour", h));
			}
			if(m != null) {
				result.append(Text.translatable("wytr.time.minute", m));
			}
			if(s != null) {
				result.append(Text.translatable("wytr.time.second", s));
			}
			if(d != null) {
				result.append(Text.translatable("wytr.time.day", d));
			}
		}
		return result;
	}
}
