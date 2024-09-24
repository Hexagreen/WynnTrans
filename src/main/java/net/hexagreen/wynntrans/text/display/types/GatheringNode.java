package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GatheringNode extends WynnDisplayText {

	private static final Pattern profRegex = Pattern.compile("§..§f .§7 (.+) Lv\\. Min: §f(\\d+)");
	private final String keyResource;
	private final String valResource;
	private final Style styleResource;
	private final String checkCross;
	private final Profession profession;
	private final Text level;
	private final String[] lines;

	public GatheringNode(Text text) {
		super(text);
		this.lines = inputText.getString().split("\\n");
		this.styleResource = parseStyleCode(lines[0]);
		this.valResource = lines[0].replaceFirst("§.", "");
		this.keyResource = rootKey + "resource." + valResource.replaceAll(" ", "");
		this.checkCross = lines[1].substring(0, 3);
		Matcher m = profRegex.matcher(lines[1]);
		boolean ignore = m.find();
		this.profession = Profession.getProfession(m.group(1));
		this.level = Text.literal(m.group(2)).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
	}

	public static boolean typeChecker(Text text) {
		return text.getString().contains("ing Lv. Min: §f");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "display.gatheringNode";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty();
		if(WTS.checkTranslationExist(keyResource, valResource)) {
			resultText.append(newTranslate(keyResource).setStyle(styleResource)).append("\n");
		}
		else {
			resultText.append(Text.literal(valResource).setStyle(styleResource)).append("\n");
		}
		Text prof = profession.getIcon().setStyle(Style.EMPTY.withColor(Formatting.WHITE)).append(" ").append(profession.getText().setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		resultText.append(newTranslate(parentKey, checkCross, prof, level).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));

		if(lines.length > 3) {
			resultText.append("\n");
			for(int i = 3; i < lines.length; i++) {
				String key = lines[i].replaceAll(".+ ", "");
				String val = lines[i].replaceFirst("§8", "");
				resultText.append("\n");
				if(WTS.checkTranslationExist(parentKey + ".get" + key, val)) {
					resultText.append(newTranslate(parentKey + ".get" + key).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
				}
				else {
					resultText.append(Text.literal(val).setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY)));
				}
			}
		}
	}
}
