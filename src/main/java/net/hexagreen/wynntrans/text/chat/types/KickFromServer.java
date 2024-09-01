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
    private final String fallbackServer;

    public KickFromServer(Text text, Pattern regex) {
        super(text, regex);
        this.channelNumber = matcher.group(1);
        Matcher m = fallbackRegex.matcher(getSibling(2).getString());
        boolean ignore = m.find();
        this.fallbackServer = m.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.serverKicked";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(newTranslate(parentKey, channelNumber).setStyle(Style.EMPTY.withColor(Formatting.RED)));

        KickReason reason = KickReason.findReason(getSibling(1));
        if(reason != KickReason.UNKNOWN_REASON) resultText.append(newTranslate(parentKey + reason.langCode));
        else resultText.append(getSibling(1));


        resultText.append(newTranslate(parentKey + ".to", fallbackServer).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }

    private enum KickReason {
        LOW_TPS("The server you were previously on went down", ".serverDown"),
        RESTART("World is restarting!", ".restart"),
        PROXY_LOST("Proxy lost connection to server.", ".proxyLost"),
        UNKNOWN_REASON(null, null);

        private final String reason;
        private final String langCode;

        KickReason(String reason, String langCode) {
            this.reason = reason;
            this.langCode = langCode;
        }

        static KickReason findReason(Text text) {
            return Arrays.stream(KickReason.values())
                    .filter(kickReason -> text.getString().contains(kickReason.reason))
                    .findFirst().orElse(UNKNOWN_REASON);
        }
    }
}
