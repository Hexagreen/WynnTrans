package net.hexagreen.wynntrans.chat;

import net.hexagreen.wynntrans.debugClass;
import net.minecraft.text.ClickEvent;
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
        return rootKey + dirFunctional + "bombStart";
    }

    @Override
    protected void build() {
        resultText = Text.empty();

        switch(bomb) {
            case COMBAT_XP, PROFESSION_XP, PROFESSION_SPEED, LOOT ->
                    resultText.append(newTranslate(parentKey, playerName, bomb.bombName)
                            .setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)))
                    .append(newTranslate(parentKey + ".durational", bomb.bombDescription, bomb.bombTime)
                            .setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)));
            case ITEM ->
                    resultText.append(newTranslate(parentKey, playerName, bomb.bombName)
                            .setStyle(Style.EMPTY.withColor(Formatting.DARK_AQUA)))
                    .append(newTranslate(parentKey + ".instant", bomb.bombDescription, bomb.bombTime)
                            .setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
            default -> {
                debugClass.writeTextAsJSON(inputText);
                resultText = inputText;
            }
        }
    }

    private static final String bombRootKey = rootKey + "bomb.";
    private enum Bombs {
        COMBAT_XP(Text.translatable(bombRootKey + "name.combat").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.combat").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "time.combat").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
        PROFESSION_XP(Text.translatable(bombRootKey + "name.profXp").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.profXp").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "time.profXp").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
        PROFESSION_SPEED(Text.translatable(bombRootKey + "name.profSpeed").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.profSpeed").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "time.profSpeed").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
        ITEM(Text.translatable(bombRootKey + "name.item").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.item").setStyle(Style.EMPTY.withColor(Formatting.WHITE)),
                Text.translatable(bombRootKey + "time.item").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/claimitembomb"))
                        .withUnderline(true))),
        SOUL_POINT(Text.translatable(bombRootKey + "name.soulPoint").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.soulPoint").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "time.soulPoint").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
        DUNGEON(Text.translatable(bombRootKey + "name.dungeon").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.dungeon").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "time.dungeon").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
        LOOT(Text.translatable(bombRootKey + "name.loot").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.loot").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "time.loot").setStyle(Style.EMPTY.withColor(Formatting.AQUA))),
        INGREDIENT(Text.translatable(bombRootKey + "name.ingredient").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.ingredient").setStyle(Style.EMPTY.withColor(Formatting.WHITE)),
                Text.translatable(bombRootKey + "time.ingredient").setStyle(Style.EMPTY.withColor(Formatting.YELLOW)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""))
                        .withUnderline(true))),
        PARTY(Text.translatable(bombRootKey + "name.party").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "desc.party").setStyle(Style.EMPTY.withColor(Formatting.AQUA)),
                Text.translatable(bombRootKey + "time.party").setStyle(Style.EMPTY.withColor(Formatting.AQUA)));

        private final Text bombName;
        private final Text bombDescription;
        private final Text bombTime;

        Bombs(Text bombName, Text bombDesc, Text bombTime) {
            this.bombName = bombName;
            this.bombDescription = bombDesc;
            this.bombTime = bombTime;
        }

        private static Bombs findBomb(String bombName) {
            switch(bombName) {
                case "Combat XP Bomb" -> {
                    return COMBAT_XP;
                }
                case "Profession XP Bomb" -> {
                    return PROFESSION_XP;
                }
                case "Profession Speed Bomb" -> {
                    return PROFESSION_SPEED;
                }
                case "Item Bomb" -> {
                    return ITEM;
                }
                case "Soul Point Bomb" -> {
                    return SOUL_POINT;
                }
                case "Dungeon Bomb" -> {
                    return DUNGEON;
                }
                case "Loot Bomb" -> {
                    return LOOT;
                }
                case "Ingredient Bomb" -> {
                    return INGREDIENT;
                }
                case "Party Bomb" -> {
                    return PARTY;
                }
                default -> {
                    return null;
                }
            }
        }
    }
}
