package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.ISpaceProvider;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class StorePurchased extends WynnChatText implements ISpaceProvider {
    public StorePurchased(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.storePurchased";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text t0 = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.GOLD).withBold(true));
        Text t1 = Text.translatable(translationKey + ".1").setStyle(GRAY);

        resultText = Text.empty();
        resultText.append(centerAlign(t0)).append("\n").append(centerAlign(t1));
    }
}
