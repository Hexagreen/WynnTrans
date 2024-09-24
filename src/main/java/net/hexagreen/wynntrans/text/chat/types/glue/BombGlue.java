package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.ChatType;
import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.BombStart;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombGlue extends TextGlue {

	private static final Pattern bombThanks = ChatType.BOMB_THANK.getRegex();

	public BombGlue() {
		super(null, BombStart.class);
	}

	@Override
	public boolean push(Text text) {
		if(gluedText.equals(Text.empty())) {
			gluedText = text.copy();
			safeNow();
			return true;
		}
		if(bombThanks.matcher(text.getString()).find()) {
			pop();
			return true;
		}
		else {
			pop();
			return false;
		}
	}
}
