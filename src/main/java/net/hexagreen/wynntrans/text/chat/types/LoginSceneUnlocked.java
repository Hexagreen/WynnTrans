package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

public class LoginSceneUnlocked extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        return text.getString().replaceAll("\\n", "").matches("Permanently unlocked the use of the .+ scene in the Character Selector!");
    }

    public LoginSceneUnlocked(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.loginSceneUnlock";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text sceneName = Text.literal(getSibling(1).getString().replaceFirst("\\n", ""))
                .setStyle(getStyle(1));
        resultText = Text.translatable(translationKey, sceneName).setStyle(getStyle(0));
    }
}
