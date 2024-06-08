package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.enums.ItemRarity;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class Identifier extends WynnChatText {
    private final MessageType messageType;

    public Identifier(Text text, Pattern regex) {
        super(text, regex);
        this.messageType = MessageType.getType(text);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "identifier";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        switch(messageType) {
            case ILLEGAL_ITEM ->
                resultText.append(newTranslate(parentKey).setStyle(getStyle(0)))
                        .append(Text.literal(": ").setStyle(getStyle(0)))
                        .append(newTranslate(parentKey + ".1").setStyle(getStyle(1)))
                        .append(ItemRarity.UNIQUE.getRarity())
                        .append(newTranslate(parentKey + ".separator").setStyle(getStyle(1)))
                        .append(ItemRarity.RARE.getRarity())
                        .append(newTranslate(parentKey + ".separator").setStyle(getStyle(1)))
                        .append(ItemRarity.LEGENDARY.getRarity())
                        .append(newTranslate(parentKey + ".separator").setStyle(getStyle(1)))
                        .append(ItemRarity.SET.getRarity())
                        .append(newTranslate(parentKey + ".separator").setStyle(getStyle(1)))
                        .append(ItemRarity.FABLED.getRarity())
                        .append(newTranslate(parentKey + ".separator_alt").setStyle(getStyle(1)))
                        .append(ItemRarity.MYTHIC.getRarity())
                        .append(newTranslate(parentKey + ".2").setStyle(getStyle(1)));
            case AUGMENT_FIRST ->
                resultText.append(newTranslate(parentKey).setStyle(getStyle(0)))
                        .append(Text.literal(": ").setStyle(getStyle(0)))
                        .append(newTranslate(parentKey + ".augmentFirst").setStyle(getStyle(1)));
            case NULL ->
                throw new UnprocessedChatTypeException(this.getClass().getName());
        }
    }

    private enum MessageType {
        ILLEGAL_ITEM(Pattern.compile("I can't identify this item! ")),
        AUGMENT_FIRST(Pattern.compile("You cannot add an augment without adding an item first!")),
        NULL(null);

        private final Pattern regex;

        MessageType(Pattern regex) {
            this.regex = regex;
        }

        private static Identifier.MessageType getType(Text text) {
            return Arrays.stream(Identifier.MessageType.values())
                    .filter(messageType -> Objects.nonNull(messageType.regex))
                    .filter(messageType -> messageType.regex.matcher(text.getString()).find())
                    .findFirst().orElse(NULL);
        }
    }
}
