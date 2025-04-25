package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Profession;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

public class CraftingEXPGain extends WynnChatText {

    public static boolean typeChecker(Text text) {
        return text.getString().matches("§7\\[\\+\\d+ §f[ⒶⒹⒺⒻⒼⒽⒾⓁ] §7.+ XP] §6\\[\\d+%]");
    }

    public CraftingEXPGain(Text text) {
        super(colorCodedToStyled(text));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.craftingXP";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        String amount = getContentString(0).replaceAll("([\\[ ])", "");
        Profession profession = Profession.getProfession(getContentString(1).charAt(0));
        Text profName = profession.getTextWithIcon().setStyle(GRAY);
        Text percent = getSibling(3);

        resultText = Text.translatable(translationKey, amount, profName, percent).setStyle(GRAY);
    }
}
