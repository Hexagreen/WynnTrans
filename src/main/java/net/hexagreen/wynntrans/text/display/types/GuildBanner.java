package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class GuildBanner extends WynnDisplayText {

	private final String keyAreaName;
	private final String valAreaName;
	private final String owner;
	private final String[] lines;

	public static boolean typeChecker(Text text) {
		return text.getString().contains("\n§7Controlled by ");
	}

	public GuildBanner(Text text) {
		super(text);
		this.lines = inputText.getString().split("\\n");
		this.valAreaName = lines[0].replaceAll("§.", "");
		this.keyAreaName = rootKey + "area." + normalizeStringForKey(valAreaName);
		this.owner = lines[1].replaceFirst("§7Controlled by ", "");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "display.guildBanner";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty();
		if(WTS.checkTranslationExist(keyAreaName, valAreaName)) {
			resultText.append(newTranslate(keyAreaName).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withBold(true)));
		}
		else {
			resultText.append(Text.literal(valAreaName).setStyle(Style.EMPTY.withColor(Formatting.WHITE).withBold(true)));
		}

		resultText.append("\n").append(newTranslate(parentKey + ".controlBy", owner).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n").append(lines[2]).append("\n").append(newTranslate(rootKey + "func.clickForOptions").setStyle(Style.EMPTY.withColor(Formatting.RED)));
	}
}
