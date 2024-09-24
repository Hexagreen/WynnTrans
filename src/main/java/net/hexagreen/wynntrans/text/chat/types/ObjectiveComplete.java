package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Objectives;
import net.hexagreen.wynntrans.text.chat.ICenterAligned;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObjectiveComplete extends WynnChatText implements ICenterAligned {

	private static final Pattern REGEX_EXP = Pattern.compile("§7\\+§f(\\d+)§7 Experience Points");
	private static final Pattern REGEX_EME = Pattern.compile("\\+(\\d+) Emeralds");
	private static final String func = rootKey + "func.";
	private final Style titleStyle;
	private final Style objectiveNameStyle;
	private String keyObjectiveName;
	private String valObjectiveName;
	private Object argObjectiveName = new Object[0];
	private String keyEName = null;
	private String valEName = null;
	private Style styleEName = null;
	private Style styleEReward = null;

	public ObjectiveComplete(Text text, Pattern regex) {
		super(text, regex);
		this.titleStyle = parseStyleCode(getSibling(1).getString().replaceAll("\\[.+]", "").replaceAll(" ", ""));

		this.objectiveNameStyle = parseStyleCode(getSibling(2).getString().replaceAll("(?!§.) +.+", ""));
		this.valObjectiveName = getSibling(2).getString().replaceAll("^§. +", "");
		this.keyObjectiveName = parentKey + valObjectiveName.replace(" ", "");
		normalizeKeyVal();

		if(text.getSiblings().size() == 8) {
			this.styleEName = parseStyleCode(getSibling(4).getString());
			this.valEName = getSibling(4).getString().replaceAll(" +(§.)+", "");
			String hash2 = DigestUtils.sha1Hex(valEName).substring(0, 4);
			this.keyEName = "wytr.eventInfo.eventName." + hash2;
			this.styleEReward = parseStyleCode(getSibling(5).getString());
		}
	}

	@Override
	protected String setParentKey() {
		return rootKey + "objective.";
	}

	@Override
	protected void build() {
		resultText = Text.empty().append("\n").append(getCenterIndent(func + "objCompleted")).append(newTranslate(func + "objCompleted").setStyle(titleStyle)).append("\n");

		if(WTS.checkTranslationExist(keyObjectiveName, valObjectiveName)) {
			resultText.append(getCenterIndent(keyObjectiveName, argObjectiveName)).append(newTranslate(keyObjectiveName, argObjectiveName).setStyle(objectiveNameStyle));
		}
		else {
			resultText.append(getCenterIndent(Text.literal(valObjectiveName))).append(Text.literal(valObjectiveName).setStyle(objectiveNameStyle));
		}

		resultText.append("\n\n");

		if(titleStyle.equals(Style.EMPTY.withColor(Formatting.DARK_GREEN))) {
			resultText.append(newTranslate(func + "info").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE))).append("\n");

			for(int i = 4; getSiblings().size() > i; i++) {
				Matcher m1 = REGEX_EXP.matcher(getSibling(i).getString());
				if(m1.find()) {
					resultText.append("     - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)).append(newTranslate(func + "reward.experience", m1.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n");
					continue;
				}
				Matcher m2 = REGEX_EME.matcher(getSibling(i).getString());
				if(m2.find()) {
					resultText.append("     - ").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)).append(newTranslate(func + "reward.emerald", m2.group(1)).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n");
					continue;
				}
				resultText.append(getSibling(i));
				resultText.append("\n");
			}

			return;
		}

		if(keyEName != null) {
			if(WTS.checkTranslationExist(keyEName, valEName)) {
				Text text = newTranslate(keyEName).setStyle(styleEName);
				resultText.append(getCenterIndent(text)).append(text);
			}
			else {
				Text origin = Text.literal(valEName).setStyle(styleEName);
				resultText.append(getCenterIndent(origin)).append(origin);
			}
			Text origin = Text.literal(getSibling(5).getString().replaceAll(" +§.", "")).setStyle(styleEReward);
			resultText.append("\n").append(getCenterIndent(origin)).append(origin).append("\n\n");
		}

		resultText.append(getCenterIndent(func + "objReward")).append(newTranslate(func + "objReward").setStyle(getSibling(-1).getSiblings().get(1).getStyle())).append("\n");
	}

	private void normalizeKeyVal() {
		Objectives normalizedObjective = Objectives.findNormalized(valObjectiveName);
		if(normalizedObjective != Objectives.NO_TYPE) {
			this.keyObjectiveName = parentKey + normalizedObjective.getNormalizedKey();
			this.valObjectiveName = normalizedObjective.getNormalizedVal();
			this.argObjectiveName = normalizedObjective.getNormalizedArg();
		}
	}
}
