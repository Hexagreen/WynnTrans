package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.ObjectiveComplete;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class ObjectiveGlue extends TextGlue {

	private static final Pattern FESTIVAL = Pattern.compile("^ +(?:§.)+Festival of the .+$");
	private static final Pattern CLAIM = Pattern.compile("^ +Click here to claim your rewards!$");
	private static final Pattern REWARD = Pattern.compile("^§d {5}- §7\\+.+$");
	private int count = 0;

	public ObjectiveGlue() {
		super(null, ObjectiveComplete.class);
		gluedText.append(" ");
		count++;
	}

	@Override
	public boolean push(Text text) {
		if(text.getString().equals("\n")) return true;
		switch(count) {
			case 1, 2, 3, 5, 6 -> {
				resetTimer();
				gluedText.append(text);
				count++;
				return true;
			}
			default -> {
				if(FESTIVAL.matcher(text.getString()).find()) {
					resetTimer();
					gluedText.append(text);
					count++;
					return true;
				}
				if(CLAIM.matcher(text.getString()).find()) {
					safeNow();
					resetTimer();
					gluedText.append(text);
					pop();
					return true;
				}
				if(REWARD.matcher(text.getString()).find()) {
					safeNow();
					resetTimer();
					gluedText.append(text);
					return true;
				}
				else {
					pop();
					return false;
				}
			}
		}
	}
}
