package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class MobTotemDeployed extends WynnChatText {
    private final Text playerName;
    private final Text location;
    private final Text coordinate;

    public static boolean typeChecker(Text text) {
        return Pattern.compile(" has placed a mob totem in ").matcher(text.getString()).find();
    }

    public MobTotemDeployed(Text text) {
        super(text);
        this.playerName = getSibling(0);
        this.location = getLocation(getSibling(2));
        this.coordinate = getSibling(4);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.mobTotemDeployed";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA));
        resultText.append(Text.translatable(parentKey, playerName, location, coordinate));
    }

    private Text getLocation(Text text) {
        String areaName = normalizeStringForKey(text.getString());
        return Text.translatable(parentKey + "area." + areaName);
    }
}
