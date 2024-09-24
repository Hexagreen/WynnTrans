package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class RaidKeeper extends WynnDisplayText {

	private final Text raidName;
	private final Text cost;

	public RaidKeeper(Text text) {
		super(text);
		this.raidName = getRaidName(getSibling(2));
		this.cost = getSibling(4).getSiblings().getFirst();
	}

	public static boolean typeChecker(Text text) {
		return text.getString().contains("\uE011\uE000\uE008\uE003 \uE00A\uE004\uE004\uE00F\uE004\uE011\uDB00\uDC02");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "display.raidKeeper.cost";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty();
		resultText.append(getSibling(0)).append("\n");
		resultText.append(raidName);
		resultText.append("\n\n");
		resultText.append(newTranslate(parentKey, cost).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
	}

	private Text getRaidName(Text text) {
		Style style = text.getStyle();
		String valName = text.getString();
		String keyName = "wytr.raid." + normalizeStringForKey(valName);
		if(WTS.checkTranslationExist(keyName, valName)) {
			return newTranslate(keyName).setStyle(style);
		}
		else {
			return Text.literal(valName).setStyle(style);
		}
	}
}
