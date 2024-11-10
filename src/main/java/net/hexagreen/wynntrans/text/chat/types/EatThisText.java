package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

/**
 * This class will destroy income text
 */
public class EatThisText extends WynnChatText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§dYou've revealed:").matcher(text.getString()).find();
    }

    public EatThisText(Text text) {
        super(text);
    }

    @Override
    protected String setParentKey() {
        return null;
    }

    @Override
    protected void build() {
        resultText = null;
    }
}
