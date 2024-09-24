package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class Blacksmith extends WynnSystemText {

	public Blacksmith(Text text, Pattern regex) {
		super(text, regex);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.blacksmith";
	}

	@Override
	protected void build() {
		resultText = Text.empty().append(header).setStyle(getStyle());

		resultText.append(newTranslate(parentKey).append(": "));

		if(getContentString(1).contains("I can't buy")) {
			resultText.append(newTranslateWithSplit(parentKey + ".no").setStyle(getStyle(1)));
		}
		else if(getContentString(1).contains("You have sold")) {
			Text soldAmount = getSoldAmount();
			Text earnedEmerald = getEarnedEmerald();
			resultText.append(newTranslateWithSplit(parentKey + ".sold", soldAmount, earnedEmerald).setStyle(getStyle(1)));
		}
		else if(getContentString(1).contains("You have repaired")) {
			Text[] parsed = getRepairing();
			Text repairedItem = parsed[0];
			Text repairCost = parsed[1];
			resultText.append(newTranslateWithSplit(parentKey + ".repair", repairedItem, repairCost).setStyle(getStyle(1)));
		}

		else throw new TextTranslationFailException("Blacksmith.class");
	}

	private Text getSoldAmount() {
		return Text.literal(getContentString(2).replaceAll("\\D", "")).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
	}

	private Text getEarnedEmerald() {
		return Text.literal(getContentString(4).replaceAll(" ?\\n ?", " ")).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
	}

	private Text[] getRepairing() {
		MutableText item = Text.empty();
		MutableText cost = Text.empty();
		boolean metFor = false;
		for(int i = 2; i < getSiblings().size(); i++) {
			if(getSibling(i).getString().contains("for ")) {
				metFor = true;
				continue;
			}
			if(metFor) cost.append(getSibling(i));
			if(!metFor) item.append(Text.literal(getSibling(i).getString().replaceAll("\n", "")).setStyle(getStyle(i)));
		}
		return new Text[]{item, cost};
	}

}
