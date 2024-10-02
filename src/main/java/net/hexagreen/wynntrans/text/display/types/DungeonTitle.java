package net.hexagreen.wynntrans.text.display.types;

import net.hexagreen.wynntrans.enums.Dungeons;
import net.hexagreen.wynntrans.text.display.WynnDisplayText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Locale;

public class DungeonTitle extends WynnDisplayText {
    private final Text dungeonName;
    private final Text difficulty;
    private final String level;

    public static boolean typeChecker(Text text) {
        if(text.getSiblings().size() != 2) return false;
        return text.getString().contains("\uE003\uE014\uE00D\uE006\uE004\uE00E\uE00D\uDB00\uDC02");
    }

    public DungeonTitle(Text text) {
        super(text);
        String[] split = getSibling(1).getString().split("\\n");
        this.dungeonName = getDungeonName(split[1]);
        this.difficulty = getDifficulty(split[3].split(" ")[1]);
        this.level = split[3].replaceAll(".+: (.+)$", "$1");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "display.dungeon";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        resultText = Text.empty().setStyle(getStyle());
        resultText.append(getSibling(0)).append("\n").append(newTranslate(parentKey, dungeonName, difficulty, level).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
    }

    private Text getDungeonName(String string) {
        Style style = parseStyleCode(string);
        return Dungeons.getDungeons(string).getDungeonName().setStyle(style);
    }

    private Text getDifficulty(String string) {
        Style style = parseStyleCode(string);
        String diff = string.replaceFirst("(?:§.)+", "").toLowerCase(Locale.ENGLISH);
        return newTranslate("wytr.difficulty." + diff).setStyle(style);
    }
}
