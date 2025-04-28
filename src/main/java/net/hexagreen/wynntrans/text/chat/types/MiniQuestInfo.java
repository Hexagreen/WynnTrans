package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.hexagreen.wynntrans.text.tooltip.types.ItemName;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class MiniQuestInfo extends WynnChatText {
    private final boolean gatheringQuest;

    public MiniQuestInfo(Text text) {
        super(text);
        this.gatheringQuest = text.getString().contains("Gathering");
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "func.miniQuestInfo";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        if(gatheringQuest) {
            Text _item0 = getSibling(0);
            Text _item1 = getSibling(2);
            String[] split0 = _item0.getString().replaceAll("[\\[\\]]", "").split(" ", 2);
            String[] split1 = _item1.getString().replaceAll("[\\[\\]]", "").split(" ", 2);
            Text item0 = Text.translatable("wytr.func.questingItem", split0[0], new ItemName(split0[1]).textAsMutable()).setStyle(_item0.getStyle());
            Text item1 = Text.translatable("wytr.func.questingItem", split1[0], new ItemName(split1[1]).textAsMutable()).setStyle(_item1.getStyle());

            resultText.append(Text.translatable(translationKey + ".gathering", item0, item1, getSibling(4))).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
            return;
        }
        Text _item = getSibling(0);
        String[] split = _item.getString().replaceAll("[\\[\\]]", "").split(" ", 2);
        Text item = Text.translatable("wytr.func.questingItem", split[0], new ItemName(split[1]).textAsMutable()).setStyle(_item.getStyle());

        resultText.append(Text.translatable(translationKey + ".slaying", item, getSibling(2)).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    }
}
