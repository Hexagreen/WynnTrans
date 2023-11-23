package net.hexagreen.wynntrans.chat;

import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RecruitMessage extends WynnChatText implements ICenterAligned {

    protected RecruitMessage(Text text, Pattern regex) {
        super(text, regex);
    }

    public static RecruitMessage of(Text text, Pattern regex) {
        return new RecruitMessage(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "recruit";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n");
        Text t0 = newTranslate(parentKey);
        Text t1 = newTranslate(parentKey + "_1");
        Text t2 = newTranslate(parentKey + "_2").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/recruit")));
        Text t3 = newTranslate(parentKey + "_3");

        resultText.append(getCenterIndent(t0)).append(t0).append("\n")
                .append(getCenterIndent(t1)).append(t1).append("\n\n")
                .append(getCenterIndent(t2)).append(t2).append("\n")
                .append(getCenterIndent(t3)).append(t3).append("\n");
    }
}