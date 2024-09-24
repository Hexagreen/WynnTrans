package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NewQuest extends WynnChatText {

	private final String keyQuestName;
	private final String valQuestName;
	private final boolean questLineMode;
	private final boolean miniQuestMode;

	public NewQuest(Text text, Pattern regex) {
		super(text, regex);
		this.questLineMode = getContentString().contains("Questline");
		this.miniQuestMode = getContentString().contains("Mini-Quest");
		this.valQuestName = getContentString(0);
		this.keyQuestName = parentKey + normalizeStringForKey(valQuestName);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "quest.";
	}

	@Override
	protected void build() {
		if(questLineMode) {
			resultText = newTranslate(rootKey + "func.newQuestline").setStyle(getStyle()).append(": ");
		}
		else if(miniQuestMode) {
			resultText = newTranslate(rootKey + "func.newMiniQuest").setStyle(getStyle()).append(": ");
		}
		else {
			resultText = newTranslate(rootKey + "func.newQuest").setStyle(getStyle()).append(": ");
		}

		if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
			resultText.append(newTranslate(keyQuestName).setStyle(getSibling(0).getStyle()));
		}
		else {
			resultText.append(getSibling(0));
		}
	}
}
