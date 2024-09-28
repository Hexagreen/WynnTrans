package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.text.tooltip.WynnTooltipText;
import net.minecraft.text.Text;

import java.util.List;

public class ContentBookFilterAndSort extends WynnTooltipText {

	public static boolean typeChecker(List<Text> text) {
		if(text.getFirst().getString().equals("Filter")) return true;
		if(text.getFirst().getString().equals("Sort")) return true;
		return false;
	}

	public ContentBookFilterAndSort(List<Text> text) {
		super(colorCodedToStyledBatch(text));
	}

	@Override
	protected String setParentKey() {
		return null;
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = new SimpleTooltip(getSiblings().subList(0, getSiblings().size() - 3)).textRaw();
		resultText.append(" ")
				.append(newTranslate("wytr.tooltip.nextPage"))
				.append(newTranslate("wytr.tooltip.previousPage"));
	}
}
