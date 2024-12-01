package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class JoinQueue extends WynnChatText implements ISpaceProvider {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^\\n +§b§lYou are in queue to join!").matcher(text.getString()).find();
    }

    public JoinQueue(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.joinQueue";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().append("\n");
        Text t0 = Text.translatable(translationKey);
        Text t1 = Text.translatable(translationKey + ".guide");

        resultText.append(getCenterIndent(t0)).append(t0).append("\n").append(getCenterIndent(t1)).append(t1).append("\n");
    }
}
