package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.regex.Pattern;

public class InfoSale extends WynnChatText {

	private final String hash;
	private final Text original;
	private final Text timer;

	public InfoSale(Text text, Pattern regex) {
		super(cutoffTail(text), regex);
		this.original = text;
		this.hash = DigestUtils.sha1Hex(inputText.getString()).substring(0, 8);
		this.timer = initTimer(text.getSiblings().getLast());
	}

	private static Text cutoffTail(Text text) {
		MutableText result = Text.empty();
		for(int i = 0; text.getSiblings().size() - 2 > i; i++) {
			result.append(text.getSiblings().get(i));
		}
		return result;
	}

	@Override
	protected String setParentKey() {
		return rootKey + "saleInfo";
	}

	@Override
	protected void build() throws IndexOutOfBoundsException {
		resultText = Text.empty();
		resultText.append(newTranslate(parentKey).setStyle(getStyle(0)));

		for(int index = 1; getSiblings().size() > index; index++) {
			String keySibling = parentKey + "." + hash + "_" + (index - 1);
			String valSibling = getContentString(index);
			if(valSibling.isEmpty()) resultText.append("");
			if(valSibling.contains("wynncraft.com")) {
				resultText.append(getSibling(index));
				continue;
			}
			if(WTS.checkTranslationExist(keySibling, valSibling)) {
				resultText.append(newTranslate(keySibling).setStyle(getStyle(index)));
			}
			else {
				resultText.append(getSibling(index));
			}
		}

		resultText.append(newTranslate(parentKey + ".end", timer).setStyle(original.getSiblings().get(original.getSiblings().size() - 2).getStyle()));
	}

	private Text initTimer(Text text) {
		String str = text.getString().replace(".", "");
		return Text.literal(str).setStyle(text.getStyle());
	}
}
