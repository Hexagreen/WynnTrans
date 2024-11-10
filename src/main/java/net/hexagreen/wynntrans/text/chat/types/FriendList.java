package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class FriendList extends WynnSystemText {
    private final String playerName;
    private final String friendNumber;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("(.+)'s friends \\((\\d+)\\): ").matcher(removeTextBox(text)).find();
    }

    public FriendList(Text text) {
        super(text, Pattern.compile("(.+)'s friends \\((\\d+)\\): "));
        this.playerName = matcher.group(1);
        this.friendNumber = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.friendList";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append(header).setStyle(getStyle());
        resultText.append(Text.translatable(parentKey, playerName, friendNumber));

        String[] friends = getSibling(0).getString().replaceFirst("^.+'s friends \\(\\d+\\): ", "").split("\\n ");

        resultText.append(Text.literal(friends[0]).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
        for(int i = 1; i < friends.length; i++) {
            resultText.append(splitter);
            resultText.append(Text.literal(friends[i]).setStyle(Style.EMPTY.withColor(Formatting.GOLD)));
        }
    }
}
