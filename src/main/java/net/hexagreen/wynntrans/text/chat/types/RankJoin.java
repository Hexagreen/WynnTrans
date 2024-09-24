package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RankJoin extends WynnChatText {

	public RankJoin(Text text, Pattern regex) {
		super(text, regex);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.rankJoin";
	}

	@Override
	protected void build() {
		resultText = Text.empty();
		resultText.append(getSibling(0)).append(getSibling(1)).append(newTranslate(parentKey).setStyle(getStyle(2)));
	}
}
