package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.ICenterAligned;
import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class JoinQueue extends WynnChatText implements ICenterAligned {

    public JoinQueue(Text text, Pattern regex) {
        super(text, regex);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.joinQueue";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, UnprocessedChatTypeException {
        resultText = Text.empty().append("\n");
        Text t0 = newTranslate(parentKey);
        Text t1 = newTranslate(parentKey + ".guide");

        resultText.append(getCenterIndent(t0)).append(t0).append("\n")
                .append(getCenterIndent(t1)).append(t1).append("\n");
    }
}
