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
    protected String setTranslationKey() {
        return rootKey + "func.recruit";
    }

    @Override
    protected void build() {
        resultText = Text.empty().append("\n");
        Text t0 = Text.translatable(translationKey);
        Text t1 = Text.translatable(translationKey + ".1");
        Text t2 = Text.translatable(translationKey + ".2").setStyle(Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/recruit")));
        Text t3 = Text.translatable(translationKey + ".3");

        resultText.append(centerAlign(t0)).append("\n").append(centerAlign(t1)).append("\n\n").append(centerAlign(t2)).append("\n").append(centerAlign(t3)).append("\n");
    }
}
