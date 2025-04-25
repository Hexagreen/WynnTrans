package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class SkinApplied extends WynnChatText {
    private final boolean weaponMode;
    private final String skinName;

    public static boolean typeChecker(Text text) {
        return Pattern.compile("^§7You have set your (weapon|helmet) skin to (.+)$").matcher(text.getString()).find();
    }

    public SkinApplied(Text text) {
        super(text);
        this.weaponMode = inputText.getString().contains("your weapon skin");
        this.skinName = inputText.getString().replaceFirst(".+skin to (.+)$", "$1");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.skinApplied.";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException {
        String key = weaponMode ? translationKey + "weapon" : translationKey + "helmet";

        resultText = Text.empty();
        resultText.append(Text.translatable(key, skinName).setStyle(GRAY));
    }
}
