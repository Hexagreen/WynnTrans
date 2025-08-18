package net.hexagreen.wynntrans.text.tooltip.types;

import net.hexagreen.wynntrans.enums.Dungeons;
import net.hexagreen.wynntrans.text.tooltip.AbstractCurrencyItem;
import net.hexagreen.wynntrans.text.tooltip.ITooltipSplitter;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class DungeonItems extends AbstractCurrencyItem {
    private Dungeons dungeon;
    private boolean keyDescAppended;
    private Style keyNameStyle;

    public static boolean typeChecker(List<Text> texts) {
        if(texts.isEmpty()) return false;
        String name = texts.getFirst().getString()
                .replaceAll("§.|À+$", "")
                .replaceAll(" Key$| Fragment$|^Broken ", "");
        return Dungeons.getDungeons(name) != Dungeons.UNKNOWN;
    }

    public static MutableText getTranslatedItemName(Text text) {
        String _iName = text.getString()
                .replaceAll("§.|À+$", "")
                .replaceAll(" Key$| Fragment$|^Broken ", "");
        if(Dungeons.getDungeons(_iName) == Dungeons.UNKNOWN) return null;
        List<Text> textBowl = new ArrayList<>();
        textBowl.add(text);
        textBowl.add(Text.empty());
        DungeonItems dungeonItems = new DungeonItems(textBowl);
        dungeonItems.translateNameSection(dungeonItems.inputText.getSiblings());
        return dungeonItems.resultText.getSiblings().getFirst().copy();
    }

    public DungeonItems(List<Text> texts) {
        super(ITooltipSplitter.correctSplitter(texts));
        this.dungeon = Dungeons.UNKNOWN;
        this.keyDescAppended = false;
        this.keyNameStyle = Style.EMPTY;
    }

    @Override
    protected String setTranslationKey() {
        return "";
    }

    @Override
    protected void translateNameStep(Text text) {
        if(keyDescAppended) return;
        Text line = text.getSiblings().getFirst();
        String _string = line.getString().replaceAll("À", "");
        if(_string.matches("^Broken .+")) {
            this.dungeon = Dungeons.getDungeons(_string.replaceAll("^Broken | Key$", ""));
            resultText.append(Text.translatable("wytr.item.misc.brokenDungeonKey", dungeon.getDungeonName())
                    .setStyle(line.getStyle()));
            keyNameStyle = line.getStyle();
        }
        else if(_string.matches(".+ Key$")) {
            this.dungeon = Dungeons.getDungeons(_string.replaceAll(" Key$", ""));
            resultText.append(Text.translatable("wytr.item.misc.dungeonKey", dungeon.getDungeonName())
                    .setStyle(line.getStyle()));
            keyNameStyle = line.getStyle();
        }
        else if(_string.matches(".+ Fragment$")) {
            this.dungeon = Dungeons.getDungeons(_string.replaceAll(" Fragment$", ""));
            resultText.append(dungeon.getDungeonFragment().copy().setStyle(line.getStyle()));
        }
        else if(_string.equals("Use this item at the")) {
            wrapLine(Text.translatable("wytr.item.misc.brokenDungeonKey.desc",
                    dungeon.getDungeonName().formatted(Formatting.DARK_RED)
            ).setStyle(GRAY), 150).forEach(resultText::append);
            this.keyDescAppended = true;
        }
        else if(_string.equals("Grants access to the")) {
            wrapLine(Text.translatable("wytr.item.misc.dungeonKey.desc",
                    dungeon.getDungeonName().formatted(Formatting.WHITE)
            ).setStyle(GRAY), 150).forEach(resultText::append);
            this.keyDescAppended = true;
        }
        else {
            this.dungeon = Dungeons.getDungeons(_string);
            if(dungeon != Dungeons.UNKNOWN) {
                resultText.append(dungeon.getDungeonBossReward().copy().setStyle(line.getStyle()));
            }
            else resultText.append(text);
        }
    }

    @Override
    protected void additionalStep(String _string, List<Text> seg) {
        if(_string.equals("Grants access to the") || _string.equals("Use this item at the")) {
            translateNameSection(seg);
        }
        else if(_string.equals("Dungeon Info:")) {
            translateDungeonInfoSection(seg);
        }
        else if(_string.contains("Can be exchanged for items")) {
            translateDescSection(seg);
        }
        else if(_string.equals("Untradable Item")) {
            translateLoreSection(seg);
        }
        else {
            seg.forEach(resultText::append);
        }
    }

    private void translateDungeonInfoSection(List<Text> segment) {
        for(int i = 0, segmentSize = segment.size(); i < segmentSize; i++) {
            Text text = segment.get(i);
            MutableText line = Text.empty().setStyle(GRAY)
                    .append(Text.literal(" - ").setStyle(keyNameStyle));
            if(i == 0) resultText.append(Text.translatable("wytr.tooltip.dungeonInfo").setStyle(keyNameStyle));
            else if(text.getString().contains("Combat Lv: ")) {
                Text _level = text.getSiblings().getLast();
                line.append(Text.translatable("wytr.tooltip.compassItems.playerInfo.combatLevel", _level));
                resultText.append(line);
            }
            else if(text.getString().contains("Coordinates: ")) {
                Text _coordinates = text.getSiblings().getLast();
                line.append(Text.translatable("wytr.tooltip.dungeonInfo.coord", _coordinates));
                resultText.append(line);
            }
            else segment.forEach(resultText::append);
        }
    }

    private void translateDescSection(List<Text> segment) {
        Text shop = Text.translatable("wytr.item.misc.dungeonReward.shop", dungeon.getDungeonName()).formatted(Formatting.WHITE);
        wrapLine(Text.translatable("wytr.item.misc.dungeonReward.desc", shop).setStyle(GRAY), 150)
                .forEach(resultText::append);
    }

    private void translateLoreSection(List<Text> segment) {
        for(Text t : segment) {
            if(t.getString().equals("Untradable Item")) {
                resultText.append(Text.translatable("wytr.tooltip.item.untradable").formatted(Formatting.RED));
            }
            else {
                wrapLine(dungeon.getDungeonBossRewardLore().formatted(Formatting.DARK_GRAY), 150)
                        .forEach(resultText::append);
                break;
            }
        }
    }

}
