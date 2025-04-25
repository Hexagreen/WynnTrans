package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class MobTotem extends WynnDisplayText {
    private final String playerName;
    private final String time;

    public static boolean typeChecker(Text text) {
        if(!text.getSiblings().isEmpty()) return false;
        return text.getString().matches(".+§6§l Mob Totem\\n§c§l.+");
    }

    public MobTotem(Text text) {
        super(text);
        this.playerName = getContentString().replaceFirst("(?s)'s.+", "");
        this.time = getContentString().replaceFirst(".+\\n", "");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.mobTotem";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.translatable(translationKey, playerName, time);
    }
}
