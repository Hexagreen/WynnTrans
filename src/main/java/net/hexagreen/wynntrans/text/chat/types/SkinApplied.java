package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class SkinApplied extends WynnChatText {
    private final boolean weaponMode;
    private final String skinName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§7You have set your (weapon|helmet) skin to (.+)$").matcher(text.getString()).find();
    }

    public SkinApplied(Text text) {
        super(text, Pattern.compile("^§7You have set your (weapon|helmet) skin to (.+)$"));
        this.weaponMode = matcher.group(1).matches("weapon");
        this.skinName = matcher.group(2);
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.skinApplied.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        String key = weaponMode ? parentKey + "weapon" : parentKey + "helmet";

        resultText = Text.empty();
        resultText.append(Text.translatable(key, skinName).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }
}
