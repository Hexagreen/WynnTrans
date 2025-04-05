package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DeathMessage extends WynnChatText {

    public static boolean typeChecker(Text text) {
        if(NearCombatLevelUp.typeChecker(text)) return false;
        return text.getSiblings().isEmpty() && text.getStyle().equals(Style.EMPTY.withColor(Formatting.GOLD));
    }

    public DeathMessage(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = inputText;
    }
}
