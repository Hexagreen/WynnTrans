package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class RecruitMessage extends WynnChatText implements ISpaceProvider {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\n +§6§lEnjoying Wynncraft\\?").matcher(text.getString()).find();
    }

    public RecruitMessage(Text text) {
        super(text);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.recruit";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n");
        Text t0 = Text.translatable(parentKey);
        Text t1 = Text.translatable(parentKey + ".1");
        Text t2 = Text.translatable(parentKey + ".2").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/recruit")));
        Text t3 = Text.translatable(parentKey + ".3");

        resultText.append(getCenterIndent(t0)).append(t0).append("\n").append(getCenterIndent(t1)).append(t1).append("\n\n").append(getCenterIndent(t2)).append(t2).append("\n").append(getCenterIndent(t3)).append(t3).append("\n");
    }
}
