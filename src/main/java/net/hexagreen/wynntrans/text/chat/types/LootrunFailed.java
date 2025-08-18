package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.ILootrun;
import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class LootrunFailed extends WynnChatText implements ILootrun {

    public LootrunFailed(Text text) {
        super(text);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.lootrun.failed";
    }

    @Override
    protected void build() throws IndexOutOfBoundsException, TextTranslationFailException {
        Text title = Text.translatable(translationKey).formatted(Formatting.RED, Formatting.BOLD);
        Text desc = Text.translatable(translationKey + ".desc").formatted(Formatting.GRAY);

        List<Text> stats = translateStatistic(getSiblings().subList(3, getSiblings().size()));

        resultText = Text.empty()
                .append(centerAlign(title)).append("\n")
                .append(centerAlign(desc)).append("\n");
        stats.forEach(t -> resultText.append("\n").append(centerAlign(t)));
    }

    private List<Text> translateStatistic(List<Text> texts) {
        List<Text> result = new ArrayList<>();
        result.add(Text.translatable("wytr.func.lootrun.endSummary.stat").setStyle(Style.EMPTY.withColor(Formatting.LIGHT_PURPLE)));

        for(int i = 1, size = texts.size(); i < size; i++) {
            Text text = flatText(texts.get(i));
            Text number = text.getSiblings().getLast();
            String key = null;
            switch(i) {
                case 1 -> key = "wytr.func.lootrun.endSummary.stat.time";
                case 2 -> key = "wytr.func.lootrun.endSummary.stat.challenge";
            }
            if(key != null) result.add(Text.translatable(key, number).setStyle(GRAY));
        }

        return result;
    }
}
