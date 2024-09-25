package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class WorldEventComplete extends WynnSystemText implements ISpaceProvider {

	private final String experience;

	public WorldEventComplete(Text text, Pattern regex) {
		super(text, regex, true);
		this.experience = initExperience(getSibling(3));
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.worldEvent.complete";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty().append(header).setStyle(getStyle(0));

		Text t0 = Text.empty().append(getSibling(0).getSiblings().get(1)).append(getSibling(0).getSiblings().get(2));
		Text t1 = newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.WHITE));
		Text t3 = newTranslate("wytr.func.reward.experience", experience).setStyle(Style.EMPTY.withColor(0xEBF7FF));
		Text t4 = newTranslate(parentKey + ".guide").setStyle(Style.EMPTY.withColor(0xAEB8BF));

		resultText.append(splitter).append(getCenterIndent(t0).append(t0)).append(splitter).append(getCenterIndent(t1).append(t1)).append(splitter).append(splitter).append(getCenterIndent(t3).append(t3)).append(splitter).append(getCenterIndent(t4).append(t4)).append(splitter);
	}

	private String initExperience(Text text) {
		return text.getString().replaceAll("§.", "").replaceAll("\\D", "");
	}
}
