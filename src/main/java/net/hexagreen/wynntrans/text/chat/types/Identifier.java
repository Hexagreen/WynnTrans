package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.ItemRarity;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class Identifier extends WynnSystemText {

	private final MessageType messageType;

	public Identifier(Text text, Pattern regex) {
		super(text, regex);
		this.messageType = MessageType.getType(text);
	}

	@Override
	protected String setParentKey() {
		return rootKey + "func.identifier";
	}

	@Override
	protected void build() {
		resultText = Text.empty().append(header).setStyle(getStyle());
		resultText.append(newTranslate(parentKey).append(": "));

		switch(messageType) {
			case ILLEGAL_ITEM ->
					resultText.append(newTranslateWithSplit(parentKey + ".illegalItem", ItemRarity.UNIQUE.getRarity(), ItemRarity.RARE.getRarity(), ItemRarity.LEGENDARY.getRarity(), ItemRarity.SET.getRarity(), ItemRarity.FABLED.getRarity(), ItemRarity.MYTHIC.getRarity()).setStyle(getStyle(1)));

			case AUGMENT_FIRST ->
					resultText.append(newTranslateWithSplit(parentKey + ".augmentFirst").setStyle(getStyle(1)));
			case NULL -> throw new TextTranslationFailException(this.getClass().getName());
		}
	}

	private enum MessageType {
		ILLEGAL_ITEM(Pattern.compile("I can't identify")), AUGMENT_FIRST(Pattern.compile("You cannot add")), NULL(null);

		private final Pattern regex;

		MessageType(Pattern regex) {
			this.regex = regex;
		}

		private static Identifier.MessageType getType(Text text) {
			return Arrays.stream(Identifier.MessageType.values()).filter(messageType -> Objects.nonNull(messageType.regex)).filter(messageType -> messageType.regex.matcher(text.getString()).find()).findFirst().orElse(NULL);
		}
	}
}
