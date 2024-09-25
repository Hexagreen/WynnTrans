package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.apache.commons.codec.digest.DigestUtils;

public class FastTravel extends WynnDisplayText {

	private static final Text icon = Text.literal("\uE01C").setStyle(Style.EMPTY.withFont(Identifier.of("minecraft:common")));
	private final String keyTravelName;
	private final String valTravelName;
	private final String keyDestination;
	private final String valDestination;

	public static boolean typeChecker(Text text) {
		if(text.getSiblings().size() != 3) return false;
		return text.getString().contains("\uE005\uE000\uE012\uE013 \uE013\uE011\uE000\uE015\uE004\uE00B\uDB00\uDC02");
	}

	public FastTravel(Text text) {
		super(text);
		this.valTravelName = getContentString(2).split("\\n")[0];
		this.keyTravelName = parentKey + normalizeStringForKey(valTravelName);
		this.valDestination = getSibling(2).getSiblings().getFirst().getSiblings().get(2).getString();
		this.keyDestination = keyTravelName + ".dest." + DigestUtils.sha1Hex(valDestination).substring(0, 4);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "travel.";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		Text travel;
		if(WTS.checkTranslationExist(keyTravelName, valTravelName)) {
			travel = newTranslate(keyTravelName).setStyle(getStyle(2));
		}
		else travel = Text.literal(valTravelName).setStyle(getStyle(2));

		Text dest;
		if(WTS.checkTranslationExist(keyDestination, valDestination)) {
			dest = newTranslate(keyDestination).setStyle(Style.EMPTY.withItalic(true));
		}
		else {
			dest = Text.literal(valDestination).setStyle(Style.EMPTY.withItalic(true));
		}
		Text destination = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.GRAY)).append(icon).append(" ").append(dest).append(" ").append(icon).append("\n ");

		resultText = Text.empty().setStyle(getStyle());
		resultText.append(getSibling(0)).append("\n");
		resultText.append(travel).append("\n \n").append(destination);
	}
}
