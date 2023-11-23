package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GuildInfo extends WynnChatText {
    private static final Pattern WEEKLY_OBJ = Pattern.compile("^(.+) has finished their weekly objective");
    private String playerName;

    protected GuildInfo(Text text, Pattern regex) {
        super(text, regex);
        Matcher weekObj = WEEKLY_OBJ.matcher(getSibling(0).getSiblings().get(0).getString());
        if(weekObj.find()) playerName = weekObj.group(1);
    }

    public static GuildInfo of(Text text, Pattern regex) {
        return new GuildInfo(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "guildInfo";
    }

    @Override
    protected void build() {
        resultText = newTranslate(parentKey);

        if(playerName != null) {
            resultText.append(newTranslate(parentKey + ".weekObj", playerName).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));
        }
    }
}
