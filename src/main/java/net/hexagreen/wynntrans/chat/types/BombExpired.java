package net.hexagreen.wynntrans.chat.types;

import net.hexagreen.wynntrans.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class BombExpired extends WynnChatText {
    private final Text ownerName;
    private final Text bombName;

    public BombExpired(Text text, Pattern regex) {
        super(text, regex);
        this.ownerName = parsePlayerName();
        this.bombName = parseBombName(matcher.group(1));
    }

    @Override
    protected String setParentKey() {
        return rootKey + dirFunctional + "bombExpired";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(newTranslate(parentKey, ownerName, bombName).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)))
                .append(getSibling(-2));
    }

    private Text parsePlayerName() {
        if(getSibling(0).getString().isEmpty()) {
            return getSibling(1);
        }
        String strName = getSibling(0).getString().replace("'s ", "");
        return Text.literal(strName).setStyle(Style.EMPTY.withColor(Formatting.AQUA));
    }

    private Text parseBombName(String bombName) {
        if(bombName.contains("Combat")) {
            return Text.translatable(rootKey + "bomb.name.combat");
        }
        else if(bombName.contains("Profession Speed")) {
            return Text.translatable(rootKey + "bomb.name.profSpeed");

        }
        else if(bombName.contains("Profession XP")) {
            return Text.translatable(rootKey + "bomb.name.profXp");

        }
        else if(bombName.contains("Dungeon")) {
            return Text.translatable(rootKey + "bomb.name.dungeon");

        }
        else if(bombName.contains("Loot")) {
            return Text.translatable(rootKey + "bomb.name.loot");

        }
        else {
            return Text.translatable(rootKey + "bomb.name.party");
        }
    }
}
