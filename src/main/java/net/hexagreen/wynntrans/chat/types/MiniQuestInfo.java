package net.hexagreen.wynntrans.chat.types;

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
        return rootKey + dirFunctional + "miniQuestInfo";
    }

    @Override
    protected void build() {
        resultText = Text.empty();
        if(gatheringQuest) {
            resultText.append(newTranslate(parentKey + "gathering", getSibling(1), getSibling(3), getSibling(5)))
                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN));
            return;
        }
        resultText.append(newTranslate(parentKey + "slaying", getSibling(1), getSibling(3))
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
    }
}
