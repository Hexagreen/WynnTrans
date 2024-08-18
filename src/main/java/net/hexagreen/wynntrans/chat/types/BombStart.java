package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.hexagreen.wynntrans.debugClass;
import net.hexagreen.wynntrans.enums.Bombs;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class BombStart extends WynnChatText {
    private final Text playerName;
    private final Bombs bomb;

    public BombStart(Text text, Pattern regex) {
        super(text, regex);
        this.playerName = getSibling(0);
        this.bomb = Bombs.findBomb(getSibling(2).getString());
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.bombStart";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        if(bomb == null) throw new UnprocessedChatTypeException("BombStart.class");
        Text bombName = bomb.getBombName().setStyle(Style.EMPTY.withColor(Formatting.AQUA));

        switch(bomb) {
            case COMBAT_XP, PROFESSION_XP, PROFESSION_SPEED, LOOT ->
                    resultText.append(newTranslate(parentKey, playerName, bombName)
                            .setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)))
                    .append(newTranslate(parentKey + ".durational", bomb.getBombDescription(), bomb.getBombTime())
                            .setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
            case ITEM ->
                    resultText.append(newTranslate(parentKey, playerName, bombName)
                            .setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)))
                    .append(newTranslate(parentKey + ".instant", bomb.getBombDescription())
                            .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            case DUNGEON ->
                    resultText.append(newTranslate(parentKey, playerName, bombName)
                            .setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)))
                    .append(bomb.getBombDescription());
            default -> {
                debugClass.writeTextAsJSON(inputText, "UnknownBomb");
                resultText = inputText;
            }
        }
    }

}
