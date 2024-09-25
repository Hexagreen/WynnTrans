package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CombatXPGain extends WynnDisplayText {

	private static final Pattern regex = Pattern.compile("^(§.x\\d )?§.\\[§.\\+(§.\\d+)§. Combat XP§.]\\n§.(.+)");
	private final String multiplier;
	private final String experience;
	private final String owner;
	private final boolean shared;

	public static boolean typeChecker(Text text) {
		if(!text.getSiblings().isEmpty()) return false;
		return text.getString().replaceAll("§.", "").matches("^(?:x\\d )?\\[\\+\\d+ Combat XP](?:.|\\n)+");
	}

	public CombatXPGain(Text text) {
		super(text);
		Matcher matcher = regex.matcher(text.getString());
		if(matcher.find()) {
			this.multiplier = matcher.group(1);
			this.experience = matcher.group(2);
			this.owner = matcher.group(3);
			this.shared = owner.contains("[Shared]");
		}
		else throw new TextTranslationFailException("CombatXPGain.class");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "display.combatXp";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.GRAY));
		if(multiplier != null) {
			resultText.append(multiplier);
		}
		resultText.append("[");
		resultText.append(newTranslate(parentKey, experience).setStyle(Style.EMPTY.withColor(Formatting.WHITE)));
		resultText.append("]\n");
		if(shared) {
			resultText.append(newTranslate(parentKey + ".shared"));
		}
		else {
			resultText.append(owner);
		}
	}
}
