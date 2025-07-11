package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

public class HousingEnter extends WynnChatText implements ISpaceProvider {
    public HousingEnter(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.housingEnter";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty();
        String housingName = getContentString(0).replaceFirst("^ +", "");
        String housingOwner = getContentString(1).replaceFirst("^ +§7by ", "");
        String housingDesc = getContentString(3).replaceFirst("^ +", "");
        Text translated = Text.translatable(translationKey, housingName, housingOwner, housingDesc).setStyle(GRAY);
        wrapLine(translated, (int) CHAT_HUD_WIDTH).forEach(t -> resultText.append(centerAlign(t).append("\n")));
    }
}
