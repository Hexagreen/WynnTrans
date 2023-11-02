package net.hexagreen.wynntrans.chat;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class SkipTutorial extends WynnChatText {

    protected SkipTutorial(Text text, Pattern regex) {
        super(text, regex);
    }

    public static SkipTutorial of(Text text, Pattern regex) {
        return new SkipTutorial(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "skipTutorial";
    }

    @Override
    protected void build() {
        Text command = Text.literal("/skiptutorial").setStyle(
                Style.EMPTY.withColor(Formatting.RED)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/skiptutorial")));

        resultText = Text.empty();
        resultText.append(newTranslate(parentKey + "_1").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)))
                .append(command)
                .append(newTranslate(parentKey + "_2").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED)));
    }
}
