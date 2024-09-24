package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class ProfessionLevelAnnounce extends WynnChatText {

	private final String level;
	private final Text playerName;
	private final Text profession;

	public ProfessionLevelAnnounce(Text text, Pattern regex) {
		super(text, regex);
		this.level = "§f" + matcher.group(2);
		String profIcon = matcher.group(3) + " ";
		String keyProfName = "wytr.profession." + matcher.group(4).toLowerCase();
		this.playerName = Text.literal(matcher.group(1));
		this.profession = Text.literal(profIcon).setStyle(Style.EMPTY.withColor(Formatting.WHITE)).append(Text.translatable(keyProfName).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
		if(!text.getSiblings().isEmpty()) throw new TextTranslationFailException("ProfessionLevelAnnounce.class");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.levelAnnounce.profession";
	}

	@Override
	protected void build() {
		resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.GRAY));
		resultText.append(Text.literal("[").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))).append(Text.literal("!")).append(Text.literal("] ").setStyle(Style.EMPTY.withColor(Formatting.DARK_GRAY))).append(newTranslate(parentKey, playerName, level, profession));
	}
}
