package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class PlayerEffectApplied extends WynnSystemText {
    private final String skinName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("You now have the (.+)\\.").matcher(removeTextBox(text)).find();
    }

    public PlayerEffectApplied(Text text) {
        super(text, Pattern.compile("You now have the (.+)\\."));
        this.skinName = matcher.group(1);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.effectApplied";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        resultText = Text.empty().append(header).setStyle(getStyle());
        resultText.append(Text.translatable(parentKey, skinName).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    }
}
