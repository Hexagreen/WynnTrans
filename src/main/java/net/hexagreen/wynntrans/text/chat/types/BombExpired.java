package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class BombExpired extends WynnChatText {
    private final Text storeLink;
    private final Text bombName;

    public BombExpired(Text text, Pattern regex) {
        super(text, regex);
        this.storeLink = Text.translatable(parentKey + ".store").setStyle(getStyle(0));
        this.bombName = parseBombName(matcher.group(1));
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.bombExpired";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        resultText.append(Text.translatable(parentKey, bombName, storeLink).setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
    }

    private Text parseBombName(String bombName) {
        if(bombName.contains("Combat XP")) {
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
