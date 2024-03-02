package net.hexagreen.wynntrans.chat.types;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KickFromServer extends WynnChatText {
    private static final String lowTPS = "The server you were previously on went down";
    private static final String restart = "World is restarting!";
    private static final Pattern fallbackRegex = Pattern.compile("You are being moved to (\\w+\\d+)\\.\\.\\.");
    private final String channelNumber;
    private final String fallbackServer;

    public KickFromServer(Text text, Pattern regex) {
        super(text, regex);
        this.channelNumber = matcher.group(1);
        Matcher m = fallbackRegex.matcher(getSibling(2).getString());
        m.find();
        this.fallbackServer = m.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "serverKicked";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        resultText.append(newTranslate(parentKey, channelNumber).setStyle(Style.EMPTY.withColor(Formatting.RED)));

        if(getSibling(1).getString().contains(lowTPS))
            resultText.append(newTranslate(parentKey + ".serverDown"));
        else if(getSibling(1).getString().contains(restart))
            resultText.append(newTranslate(parentKey + ".restart"));

        resultText.append(newTranslate(parentKey + ".to", fallbackServer).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
