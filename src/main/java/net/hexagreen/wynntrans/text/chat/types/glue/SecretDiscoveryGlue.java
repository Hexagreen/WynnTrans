package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.SecretDiscovery;
import net.minecraft.text.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecretDiscoveryGlue extends TextGlue {

	private static final Pattern DISCOVERY_AREA = Pattern.compile(" +§f.+ \\[\\d+/\\d+]");
	private int count = 0;

	public SecretDiscoveryGlue() {
		super(null, SecretDiscovery.class);
		gluedText.append(" ");
		count++;
	}

	@Override
	public boolean push(Text text) {
		if(text.getString().equals("\n")) return true;
		if(count == 1) {
			resetTimer();
			count++;
			gluedText.append(text);
			return true;
		}
		else if(count == 2) {
			Matcher m = DISCOVERY_AREA.matcher(text.getString());
			if(m.find()) {
				resetTimer();
				count++;
				gluedText.append(text);
				return true;
			}
		}
		else if(count == 3) {
			if(text.getString().equals(" ")) {
				resetTimer();
				count++;
				return true;
			}
		}
		else {
			if(text.getString().matches("^ *§7.+$")) {
				resetTimer();
				safeNow();
				gluedText.append(text);
				return true;
			}
		}
		return false;
	}
}
