package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NpcDialogLiteral extends WynnChatText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§7\\[(\\d+)/(\\d+)] §.(.+: )(.+)").matcher(text.getString()).find();
    }

    public NpcDialogLiteral(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return null;
    }

    @Override
    public void build() {
        resultText = new NpcDialog(colorCodedToStyled(inputText)).text();
    }
}
