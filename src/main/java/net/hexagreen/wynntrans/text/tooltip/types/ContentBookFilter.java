package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Text;

import java.util.List;

public class ContentBookFilter extends WynnTooltipText {

	public ContentBookFilter(List<Text> text) {
		super(colorCodedToStyledBatch(text));
	}

	public static boolean typeChecker(List<Text> text) {
		return text.getFirst().getString().equals("Filter");
	}

	@Override
	protected String setParentKey() {
		return null;
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = inputText;
		textRecorder();
	}
}
