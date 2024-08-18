package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class PlayerEffectApplied extends WynnChatText {
    private final String skinName;

    public PlayerEffectApplied(Text text, Pattern regex) {
        super(text, regex);
        this.skinName = matcher.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.effectApplied";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, skinName).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    }
}
