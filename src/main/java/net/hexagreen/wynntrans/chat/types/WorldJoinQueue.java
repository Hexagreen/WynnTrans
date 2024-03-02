package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.ICenterAligned;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class WorldJoinQueue extends WynnChatText implements ICenterAligned {
    private final String worldChannel;

    public WorldJoinQueue(Text text, Pattern regex) {
        super(text, regex);
        this.worldChannel = matcher.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "joinQueue";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n");
        Text t0 = newTranslate(parentKey, worldChannel).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true));
        Text t1 = newTranslate(parentKey + ".guide");

        resultText.append(getCenterIndent(t0)).append(t0).append("\n")
                .append(getCenterIndent(t1)).append(t1).append("\n");
    }
}
