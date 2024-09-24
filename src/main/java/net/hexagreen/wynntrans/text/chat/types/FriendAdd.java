package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class FriendAdd extends WynnChatText {

	private final String playerName;

	public FriendAdd(Text text, Pattern regex) {
		super(text, regex);
		this.playerName = matcher.group(1);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.friendAdd";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException {
		resultText = Text.empty();
		resultText.append(newTranslate(parentKey, playerName).setStyle(getStyle(0)));
	}
}
