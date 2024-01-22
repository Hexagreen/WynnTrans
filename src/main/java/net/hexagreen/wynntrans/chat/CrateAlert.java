package net.hexagreen.wynntrans.chat;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class CrateAlert extends WynnChatText {
    private final Text crateName;
    private final Text command = Text.literal("/crates").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)
            .withUnderline(true).withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/crates")));

    public CrateAlert(Text text, Pattern regex) {
        super(text, regex);
        this.crateName = Text.literal(matcher.group(1)).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE));
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "crateAlert";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, command, crateName).setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));
    }
}
