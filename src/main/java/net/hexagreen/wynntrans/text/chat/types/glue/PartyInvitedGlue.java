package net.hexagreen.wynntrans.text.chat.types.glue;

import net.hexagreen.wynntrans.text.chat.TextGlue;
import net.hexagreen.wynntrans.text.chat.types.PartyInvited;
import net.minecraft.text.Text;

public class PartyInvitedGlue extends TextGlue {

	private int count = 0;

	public PartyInvitedGlue() {
		super(null, PartyInvited.class);
	}

	@Override
	public boolean push(Text text) {
		if(count == 0) {
			resetTimer();
			gluedText.append(text);
			count++;
			return true;
		}
		if(count == 1 && text.getString().contains("Click here to join")) {
			resetTimer();
			safeNow();
			gluedText.append(text);
			pop();
			return true;
		}
		return false;
	}
}
