package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KickFromServer extends WynnChatText {
    private static final Pattern fallbackRegex = Pattern.compile("You are being moved to (\\w+\\d+)\\.\\.\\.");
    private final String channelNumber;
    private final boolean kickWhileConnect;
    private String fallbackServer;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^Kicked (?:from|whilst connecting to) (?:((?:NA|EU|AS)\\d+)|(DUMMY)): ").matcher(text.getString()).find();
    }

    public KickFromServer(Text text) {
        super(text);
        this.channelNumber = inputText.getString().replaceFirst("^Kicked (?:from|whilst connecting to) (?:((?:NA|EU|AS)\\d+)|(DUMMY)): .+", "$1");
        this.kickWhileConnect = text.getString().contains("Kicked whilst connecting");
        try {
            Matcher m = fallbackRegex.matcher(getSibling(2).getString());
            boolean ignore = m.find();
            this.fallbackServer = m.group(1);
        }
        catch(IndexOutOfBoundsException ignore) {
        }
    }

    @Override
    protected String setTranslationKey() {
        return inputText.getString().contains("Kicked whilst connecting") ? rootKey + "func.serverKickedOnConnection" : rootKey + "func.serverKicked";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(Text.translatable(translationKey, channelNumber).setStyle(Style.EMPTY.withColor(Formatting.RED)));

        KickReason reason = KickReason.findReason(getSibling(1));
        if(reason != KickReason.UNKNOWN_REASON) resultText.append(Text.translatable(translationKey + reason.langCode));
        else resultText.append(getSibling(1));

        if(!kickWhileConnect) {
            resultText.append(Text.translatable(translationKey + ".to", fallbackServer).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }
    }

    private enum KickReason {
        LOW_TPS("The server you were previously on went down", ".serverDown"),
        RESTART("World is restarting!", ".restart"),
        PROXY_LOST("Proxy lost connection to server.", ".proxyLost"),
        FAST_SWAP("You're rejoining too quickly! Give us a moment to save your data.", ".fastRejoin"),
        UNKNOWN_REASON(null, null);
        private final String reason;
        private final String langCode;

        static KickReason findReason(Text text) {
            return Arrays.stream(KickReason.values()).filter(kickReason -> text.getString().contains(kickReason.reason)).findFirst().orElse(UNKNOWN_REASON);
        }

        KickReason(String reason, String langCode) {
            this.reason = reason;
            this.langCode = langCode;
        }
    }
}
