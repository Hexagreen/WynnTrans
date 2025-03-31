package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.text.ITime;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ShamanTotem extends WynnDisplayText {

    public static boolean typeChecker(Text text) {
        return text.getString().matches(".+('s|s') Totem\\n(\\+\\d+❤/s )?\\uE01F \\d+s");
    }

    public ShamanTotem(Text text) {
        super(flatText(text));
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "display.shamanTotem";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        boolean isHealingTotem = getSibling(1).getString().contains("❤");

        Text owner = Text.literal(getContentString().replaceFirst("('s|') ", ""))
                .setStyle(Style.EMPTY.withColor(Formatting.AQUA));
        Text icon = getSiblings().get(getSiblings().size() - 3);
        Text timer = ITime.translateTime(getSiblings().getLast().getString())
                .setStyle(GRAY);

        MutableText info = Text.empty();

        if(isHealingTotem) {
            Text heal = getSibling(1);
            Text unit = ITime.translateTime(getSibling(2).getString())
                    .setStyle(GRAY);
            info.append(heal).append(unit).append(" ");
        }
        info.append(icon).append(" ").append(timer);

        resultText = Text.translatable(translationKey, owner, info).setStyle(GRAY);
    }
}
