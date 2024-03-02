package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class SkinApplied extends WynnChatText {
    private final boolean weaponMode;
    private final String skinName;

    public SkinApplied(Text text, Pattern regex) {
        super(text, regex);
        this.weaponMode = matcher.group(1).matches("weapon");
        this.skinName = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "skinApplied.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        String key = weaponMode ? parentKey + "weapon" : parentKey + "helmet";

        resultText = Text.empty();
        resultText.append(newTranslate(key, skinName).setStyle(getStyle(0)));
    }
}
