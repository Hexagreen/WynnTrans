package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class CaveProgress extends WynnDisplayText {

	private final boolean completed;
	private final String bar;

	public CaveProgress(Text text) {
		super(text);
		this.completed = text.getString().contains("Completed");
		this.bar = text.getString().split("\\n")[1];
	}

	public static boolean typeChecker(Text text) {
		return text.getString().matches("^§7Progress\\n.+");
	}

	@Override
	protected String setParentKey() {
		return rootKey + "display.caveProgress";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
		resultText = Text.empty();
		resultText.append(newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.GRAY))).append("\n");
		if(completed)
			resultText.append(newTranslate("wytr.display.caveComplete").setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
		else resultText.append(bar);
	}
}
