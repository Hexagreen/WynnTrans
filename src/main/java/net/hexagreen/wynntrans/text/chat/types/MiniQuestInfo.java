package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.regex.Pattern;

public class MiniQuestInfo extends WynnChatText {
    private final boolean gatheringQuest;

    public MiniQuestInfo(Text text, Pattern regex) {
        super(text, regex);
        this.gatheringQuest = text.getString().contains("Gathering");
    }

    @Override
    protected String setParentKey() {
        return rootKey + "func.miniQuestInfo";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        if(gatheringQuest) {
            resultText.append(Text.translatable(parentKey + ".gathering", getSibling(0), getSibling(2), getSibling(4))).setStyle(Style.EMPTY.withColor(Formatting.GREEN));
            return;
        }
        resultText.append(Text.translatable(parentKey + ".slaying", getSibling(0), getSibling(2)).setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    }
}
