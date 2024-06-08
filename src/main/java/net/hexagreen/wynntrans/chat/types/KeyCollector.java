package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;

public class KeyCollector extends WynnChatText {
    private final MessageType messageType;
    private static final Style TEXT_STYLE = Style.EMPTY.withColor(Formatting.LIGHT_PURPLE);

    public KeyCollector(Text text, Pattern regex) {
        super(text, regex);
        this.messageType = MessageType.getType(text);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "keyCollector";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(newTranslate(parentKey).setStyle(Style.EMPTY.withColor(Formatting.DARK_PURPLE)).append(": "));

        switch(messageType) {
            case GIVE_ME_KEY ->
                    resultText.append(newTranslate(parentKey + ".giveMeKey", getSibling(2))
                    .setStyle(TEXT_STYLE));
            case KEY_PASSED ->
                    resultText.append(newTranslate(parentKey + ".keyPassed")
                    .setStyle(TEXT_STYLE));
            case NEED_KEY ->
                    resultText.append(newTranslate(parentKey + ".needKey", getSibling(2))
                            .setStyle(TEXT_STYLE));
            case NEED_QUEST ->
                    resultText.append(newTranslate(parentKey + ".needQuest", getSibling(2))
                            .setStyle(TEXT_STYLE));
            case PARTY_PASS ->
                    resultText.append(newTranslate(parentKey + ".partyPass", getSibling(1), getSibling(3), parseNumber(getSibling(5)))
                    .setStyle(TEXT_STYLE));
            case NULL ->
                throw new UnprocessedChatTypeException(this.getClass().getName());
        }
    }

    private Text parseNumber(Text text) {
        String number = text.getString().replaceAll("\\D", "");
        return Text.literal(number).setStyle(text.getStyle());
    }

    private enum MessageType {
        GIVE_ME_KEY(Pattern.compile("Bring me")),
        KEY_PASSED(Pattern.compile("You have access")),
        NEED_KEY(Pattern.compile("You cannot enter")),
        NEED_QUEST(Pattern.compile("You need to complete")),
        PARTY_PASS(Pattern.compile("has already opened the entrance")),
        NULL(null);

        private final Pattern regex;

        MessageType(Pattern regex) {
            this.regex = regex;
        }

        private static MessageType getType(Text text) {
            return Arrays.stream(MessageType.values())
                    .filter(messageType -> Objects.nonNull(messageType.regex))
                    .filter(messageType -> messageType.regex.matcher(text.getString()).find())
                    .findFirst().orElse(NULL);
        }
    }
}
