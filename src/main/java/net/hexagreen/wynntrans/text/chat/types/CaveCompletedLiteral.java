package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaveCompletedLiteral extends WynnChatText implements ISpaceProvider {

	private static final Pattern REGEX_EXP = Pattern.compile("§7\\+(\\d+) Experience Points");
	private static final Pattern REGEX_EME = Pattern.compile("§7\\+(\\d+) §aEmeralds");
	private static final String func = rootKey + "func.";
	private final String keyCaveName;
	private final String valCaveName;

	private static Text splitTextBody(Text text) {
		String[] lines = text.getString().split("\\n");
		MutableText result = Text.empty();
		for(String line : lines) {
			result.append(line);
		}

		return result.copy();
	}

	public CaveCompletedLiteral(Text text, Pattern ignore) {
		super(splitTextBody(text), null);
		this.valCaveName = getSibling(2).getString().replaceAll("§. +§.", "");
		this.keyCaveName = parentKey + normalizeStringForKey(valCaveName);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "cave.";
	}

	@Override
	protected void build() {
		resultText = Text.empty().append("\n");

		Text title;
		title = newTranslate(func + "caveCompleted").setStyle(Style.EMPTY.withColor(Formatting.DARK_GREEN));

		resultText.append(getCenterIndent(title)).append(title).append("\n");


		Text t0;
		if(WTS.checkTranslationExist(keyCaveName, valCaveName)) {
			Style t0Style = parseStyleCode(getSibling(2).getString().replace(valCaveName, "").replaceAll(" ", ""));
			t0 = newTranslate(keyCaveName).setStyle(t0Style);
		}
		else {
			t0 = Text.literal(getSibling(2).getString().replaceAll(" +(?=§)", ""));
		}

		resultText.append(getCenterIndent(t0)).append(t0).append("\n\n").append(newTranslate(func + "reward").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE))).append("\n");


		for(int i = 5; getSiblings().size() > i; i++) {
			Matcher m1 = REGEX_EXP.matcher(getSibling(i).getString());
			if(m1.find()) {
				resultText.append("           - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)).append(newTranslate(func + "reward.experience", m1.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n");
				continue;
			}
			Matcher m2 = REGEX_EME.matcher(getSibling(i).getString());
			if(m2.find()) {
				resultText.append("           - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)).append(newTranslate(func + "reward.emerald", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GREEN))).append("\n");
				continue;
			}
			String valExclusiveReward = getSibling(i).getString().replaceAll("§d +- §7", "");
			String hash = DigestUtils.sha1Hex(valExclusiveReward).substring(0, 4);
			String keyExclusiveReward = keyCaveName + ".reward_" + hash;
			if(WTS.checkTranslationExist(keyExclusiveReward, valExclusiveReward)) {
				resultText.append("           - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
				resultText.append(newTranslate(keyExclusiveReward).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
			}
			else {
				resultText.append(getSibling(i));
			}
			resultText.append("\n");
		}
	}
}
