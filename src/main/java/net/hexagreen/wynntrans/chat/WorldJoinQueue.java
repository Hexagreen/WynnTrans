package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class WorldJoinQueue extends WynnChatText implements ICenterAligned {
    private final String worldChannel;

    protected WorldJoinQueue(Text text, Pattern regex) {
        super(text, regex);
        this.worldChannel = matcher.group(1);
    }

    public static WorldJoinQueue of(Text text, Pattern regex) {
        return new WorldJoinQueue(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "joinQueue";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        Text t0 = newTranslate(parentKey, worldChannel);
        Text t1 = newTranslate(parentKey + "_1");

        resultText.append(getCenterIndent(t0)).append("\n")
                .append(getCenterIndent(t1)).append("\n");
    }
}
