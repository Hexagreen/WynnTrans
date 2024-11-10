package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class KeyCollector extends WynnSystemText {
    private final MessageType messageType;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("Key Collector:").matcher(text.getString()).find();
    }

    public KeyCollector(Text text) {
        super(text, null);
        this.messageType = MessageType.getType(text);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.keyCollector";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append(header).setStyle(getStyle());

        resultText.append(Text.translatable(parentKey).append(": "));

        switch(messageType) {
            case GIVE_ME_KEY ->
                    resultText.append(newTranslateWithSplit(parentKey + ".giveMeKey", parseDungeonKey(getSibling(2))).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            case KEY_PASSED ->
                    resultText.append(newTranslateWithSplit(parentKey + ".keyPassed").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            case NEED_KEY ->
                    resultText.append(newTranslateWithSplit(parentKey + ".needKey", parseDungeonKey(getSibling(2))).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
            case PARTY_PASS -> {
                if(getSiblings().size() == 6) {
                    resultText.append(newTranslateWithSplit(parentKey + ".partyPass", parsePlayerName(getSibling(0)), parseTimeUnit(getSibling(2), getSibling(3)), parseNumber(getSibling(4))).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }
                else {
                    resultText.append(newTranslateWithSplit(parentKey + ".partyPass", getSibling(1), parseTimeUnit(getSibling(3), getSibling(4)), parseNumber(getSibling(5))).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
                }
            }
            case NULL -> throw new TextTranslationFailException("KeyCollector.class");
        }
    }

    private Text parsePlayerName(Text text) {
        String name = text.getString().substring(15);
        return Text.literal(name).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
    }

    private Text parseTimeUnit(Text time, Text text) {
        String unit = text.getString().replaceAll(", ", "");
        return ITime.translateTime(time.getString() + unit).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE));
    }

    private Text parseNumber(Text text) {
        String number = text.getString().replaceAll("\\D", "");
        return Text.literal(number).setStyle(text.getStyle());
    }

    private Text parseDungeonKey(Text text) {
        String key = text.getString().replaceAll("\\n", "");
        return Text.literal(key).setStyle(text.getStyle());
    }

    private enum MessageType {
        GIVE_ME_KEY(Pattern.compile("Bring me")), KEY_PASSED(Pattern.compile("You have access")), NEED_KEY(Pattern.compile("You cannot enter")), PARTY_PASS(Pattern.compile("has already opened")), NULL(null);
        private final Pattern regex;

        private static MessageType getType(Text text) {
            return Arrays.stream(MessageType.values()).filter(messageType -> Objects.nonNull(messageType.regex)).filter(messageType -> messageType.regex.matcher(text.getString().replaceAll("\\n", "")).find()).findFirst().orElse(NULL);
        }

        MessageType(Pattern regex) {
            this.regex = regex;
        }
    }
}
