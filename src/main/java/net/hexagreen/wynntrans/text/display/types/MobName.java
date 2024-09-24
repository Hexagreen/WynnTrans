package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MobName extends WynnDisplayText {

	private final String keyMobName;
	private final String valMobName;
	private final String subKeyMobName;
	private final Style styleMobName;
	private final boolean isRare;

	public MobName(Text text) {
		super(text);
		int index = 0;
		this.isRare = text.getString().substring(0, 1).matches("[\uE02A\uD83D\uDC31]");
		if(isRare) index = 2;
		this.valMobName = getContentString(index);
		this.keyMobName = parentKey + normalizeStringForKey(valMobName);
		this.subKeyMobName = "wytr.name." + normalizeStringForKey(valMobName);
		this.styleMobName = getStyle(index);
	}

	public static boolean typeChecker(Text text) {
		try {
			int index = 2;
			if(text.getString().substring(0, 1).matches("[\uE02A\uD83D\uDC31]")) index = 4;
			Text target = text.getSiblings().get(index).getSiblings().getFirst();
			if(target.getString().contains("\uE00B\uE015 ")) {
				if(target.getStyle().getFont().equals(Identifier.of("minecraft:banner/pill"))) {
					return true;
				}
			}
		} catch(Exception ignore) {
		}
		return false;
	}

	@Override
	protected String setParentKey() {
		return rootKey + "mobName.";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty();
		if(isRare) {
			resultText.append(getSibling(0)).append(" ");
		}
		if(WTS.checkTranslationDoNotRegister(subKeyMobName)) {
			resultText.append(newTranslate(subKeyMobName).setStyle(styleMobName));
		}
		else if(WTS.checkTranslationExist(keyMobName, valMobName)) {
			resultText.append(newTranslate(keyMobName).setStyle(styleMobName));
		}
		else {
			resultText.append(Text.literal(valMobName).setStyle(styleMobName));
		}
		for(int i = isRare ? 3 : 1; i < getSiblings().size(); i++) {
			resultText.append(getSibling(i));
		}
	}
}
