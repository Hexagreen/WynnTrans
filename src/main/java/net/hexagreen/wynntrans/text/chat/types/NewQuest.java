package net.hexagreen.wynntrans.text.chat.types;

import net.hexagreen.wynntrans.text.chat.WynnChatText;
import net.minecraft.text.Text;

public class NewQuest extends WynnChatText {
    private final String keyQuestName;
    private final String valQuestName;
    private final boolean questLineMode;
    private final boolean miniQuestMode;

    public NewQuest(Text text) {
        super(text);
        this.questLineMode = getContentString().contains("Questline");
        this.miniQuestMode = getContentString().contains("Mini-Quest");
        this.valQuestName = getContentString(0);
        this.keyQuestName = translationKey + normalizeStringForKey(valQuestName);
    }

    @Override
    protected String setTranslationKey() {
        return rootKey + "quest.";
    }

    @Override
    protected void build() {
        if(questLineMode) {
            resultText = Text.translatable(rootKey + "func.newQuestline").setStyle(getStyle()).append(": ");
        }
        else if(miniQuestMode) {
            resultText = Text.translatable(rootKey + "func.newMiniQuest").setStyle(getStyle()).append(": ");
        }
        else {
            resultText = Text.translatable(rootKey + "func.newQuest").setStyle(getStyle()).append(": ");
        }

        if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
            resultText.append(Text.translatable(keyQuestName).setStyle(getSibling(0).getStyle()));
        }
        else {
            resultText.append(getSibling(0));
        }
    }
}
