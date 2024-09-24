package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ICenterAligned;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaveCompleted extends WynnChatText implements ICenterAligned {

	private static final Pattern REGEX_EXP = Pattern.compile("^\\+(\\d+) Experience Points$");
	private static final Pattern REGEX_EME = Pattern.compile("^ +- \\+(\\d+) Emeralds$");
	private final String keyCaveName;
	private final String valCaveName;

	public CaveCompleted(Text text, Pattern regex) {
		super(text, regex);
		this.valCaveName = getSibling(2).getSiblings().get(1).getString();
		this.keyCaveName = parentKey + normalizeStringForKey(valCaveName);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "cave.";
	}

	@Override
	protected void build() {
		resultText = Text.empty().append("\n").append(getCenterIndent(rootKey + "func.caveCompleted")).append(newTranslate(rootKey + "func.caveCompleted").setStyle(getSibling(1).getSiblings().getFirst().getStyle())).append("\n");

		Text t0;
		if(WTS.checkTranslationExist(keyCaveName, valCaveName)) {
			t0 = newTranslate(keyCaveName).setStyle(getSibling(2).getSiblings().get(1).getStyle());
		}
		else {
			t0 = getSibling(2).getSiblings().get(1);
		}

		resultText.append(getCenterIndent(t0)).append(t0).append("\n\n").append(newTranslate(rootKey + "func.reward").setStyle(getSibling(4).getSiblings().getFirst().getStyle())).append("\n");


		for(int i = 5; getSiblings().size() > i; i++) {
			Matcher m1 = REGEX_EXP.matcher(getSibling(i).getSiblings().get(1).getString());
			if(m1.find()) {
				resultText.append(getSibling(i).getSiblings().get(0)).append(newTranslate(rootKey + "func.reward.experience", m1.group(1)).setStyle(getSibling(i).getSiblings().get(1).getStyle())).append("\n");
				continue;
			}
			Matcher m2 = REGEX_EME.matcher(getSibling(i).getSiblings().get(1).getString());
			if(m2.find()) {
				resultText.append(getSibling(i).getSiblings().getFirst()).append(newTranslate(rootKey + "func.reward.emerald", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GREEN))).append("\n");
				continue;
			}
			String valExclusiveReward = getSibling(i).getString();
			String hash = DigestUtils.sha1Hex(valExclusiveReward).substring(0, 4);
			String keyExclusiveReward = keyCaveName + ".reward_" + hash;
			if(WTS.checkTranslationExist(keyExclusiveReward, valExclusiveReward)) {
				resultText.append(getSibling(i).getSiblings().get(0));
				resultText.append(newTranslate(keyExclusiveReward).setStyle(getSibling(i).getSiblings().get(1).getStyle()));
			}
			else {
				resultText.append(getSibling(i));
			}
			resultText.append("\n");
		}
	}
}
