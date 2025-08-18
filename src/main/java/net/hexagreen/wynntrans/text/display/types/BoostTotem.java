package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Text;

public class BoostTotem extends WynnDisplayText {

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().isEmpty()) return false;
        return text.getString().matches(".+'s? (Mob|Gathering) Totem\\n.+");
    }

    public BoostTotem(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return getContentString(1).contains("Mob") ? rootKey + "display.mobTotem" : rootKey + "displat.gatheringTotem";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text playerName = Text.literal(getContentString(0).replaceFirst("'s?", ""))
                .setStyle(getStyle(0));
        Text icon = getSibling(2).getSiblings().getFirst();
        Text time = ITime.translateTime(getSibling(2).getSiblings().getLast());
        Text translatedTime = Text.empty().setStyle(getStyle(2)).append(icon).append(" ").append(time);
        resultText = Text.translatable(translationKey, playerName, translatedTime).setStyle(getStyle());
    }
}
