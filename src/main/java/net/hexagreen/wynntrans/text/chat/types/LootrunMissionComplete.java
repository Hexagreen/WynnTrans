package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ILootrun;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LootrunMissionComplete extends WynnChatText implements ILootrun {

    public LootrunMissionComplete(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.lootrun.missionComplete";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text title = Text.translatable(translationKey).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withBold(true));
        String clearedMissions = getSibling(1).getString().replaceAll("§.", "").replaceAll("\\D", "");
        Text desc = Text.translatable(translationKey + ".desc", clearedMissions).setStyle(GRAY);

        resultText = Text.empty()
                .append(centerAlign(title)).append("\n")
                .append(centerAlign(desc)).append("\n")
                .append("\n");

        List<List<Text>> textChunks = getTextChunks(getSiblings());
        textChunks.removeFirst();
        placeTextToBiSection(textChunks.stream()
                .map(this::mergeTextFromBiSection)
                .flatMap(Arrays::stream)
                .map(this::translateMission)
                .filter(Objects::nonNull).toList())
                .forEach(t -> resultText.append(t).append("\n"));
    }

    private List<Text> translateMission(Text text) {
        if(text.getString().isEmpty()) return null;
        List<Text> result = new ArrayList<>();

        String missionName = normalizeStringForKey(text.getSiblings().getFirst().getString().replaceAll("§.", ""));
        Style missionStyle = parseStyleCode(text.getSiblings().getFirst().getString());

        result.add(Text.translatable("wytr.lootrun.mission." + missionName).setStyle(missionStyle));
        result.addAll(wrapLine(Text.translatable("wytr.lootrun.mission." + missionName + ".desc").setStyle(GRAY), 160));

        return result;
    }
}
