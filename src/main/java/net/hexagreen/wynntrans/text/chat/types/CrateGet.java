package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class CrateGet extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 9 && text.getSiblings().size() != 7) return false;
        return Pattern.compile("(.+) has gotten an? (.+) from their crate!")
                .matcher(text.getString().replaceAll("\\n|\uDAFF\uDFFC\uE001\uDB00\uDC06", "")).find();
    }

    public CrateGet(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.crateReward";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        Object playerName = getSiblings().size() == 6 ?
                getSibling(0).getString().replaceFirst("^ ?(.+) has gotten.+", "$1") :
                getSibling(0);
        Text cosmeticName = getSibling(-5);
        Text storeLink = getSibling(-3);
        Text storeCommand = getSibling(-1);
        resultText.append(Text.translatable(translationKey, playerName, cosmeticName, storeLink, storeCommand).setStyle(getStyle()));
    }
}
