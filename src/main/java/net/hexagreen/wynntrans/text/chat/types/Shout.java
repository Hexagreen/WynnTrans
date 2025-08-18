package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class Shout extends WynnSystemText {
    public static final Pattern REGEX = Pattern.compile(".+ .+(\uE00D\uE000|\uE004\uE014|\uE000\uE012)[\uE020-\uE029]+\uDB00\uDC02 shouts: ");

    public static boolean typeChecker(Text text) {
        return REGEX.matcher(text.getString()).find();
    }

    public Shout(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.shout";
    }

    @Override
    protected void build() {
        Text name = Text.literal(getSibling(0).getString().trim()).setStyle(getStyle(0));
        Text server = Text.empty().append(getSibling(1)).append(getSibling(2));

        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, name, server).setStyle(getStyle()));
        for(int i = 4; i < getSiblings().size(); i++) {
            resultText.append(getSibling(i));
        }
    }
}
