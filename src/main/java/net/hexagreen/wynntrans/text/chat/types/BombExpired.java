package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.enums.Bombs;
import net.hexagreen.wynntrans.text.chat.WynnSystemText;
import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class BombExpired extends WynnSystemText {

    public static boolean typeChecker(Text text) {
        return Pattern.compile(" Bomb has expired! \\n\uDAFF\uDFFC\uE001\uDB00\uDC06 Get your own bombs at").matcher(text.getString()).find();
    }

    public BombExpired(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.bombExpired";
    }

    @Override
    protected void build() {
        Text playerName = getSibling(0);
        Text bombName = parseBombName();
        Text storeLink = getSibling(-1);

        resultText = Text.empty();
        resultText.append(Text.translatable(translationKey, playerName, bombName, storeLink).setStyle(getStyle()));
    }

    private Text parseBombName() {
        String bombName = getSibling(-2).getString().replaceFirst(" (.+ Bomb) has expired! \\n.+", "$1");
        Bombs bombs = Bombs.findBomb(bombName);
        return bombs != null ? bombs.getName() : Text.literal(bombName);
    }
}
