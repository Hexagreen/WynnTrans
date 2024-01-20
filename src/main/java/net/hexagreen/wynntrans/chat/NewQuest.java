package net.hexagreen.wynntrans.chat;

import net.minecraft.text.Text;

import java.util.regex.Pattern;

public class NewQuest extends WynnChatText implements IFocusText {
    private final String keyQuestName;
    private final String valQuestName;
    private final Text fullText;
    private final boolean questLineMode;
    private final boolean miniQuestMode;

    public NewQuest(Text text, Pattern regex) {
        super(text.getSiblings().get(2), regex);
        this.questLineMode = getContentString().contains("Questline");
        this.miniQuestMode = getContentString().contains("Mini-Quest");
        this.valQuestName = getContentString(0);
        this.keyQuestName = parentKey + normalizeStringQuestName(valQuestName);
        this.fullText = text;
    }

    @Override
    protected String setParentKey() {
        return rootKey + "quest.";
    }

    @Override
    protected void build() {
        if(questLineMode) {
            resultText = newTranslate(rootKey + dirFunctional + "newQuestline").setStyle(getStyle())
                    .append(": ");
        }
        else if(miniQuestMode) {
            resultText = newTranslate(rootKey + dirFunctional + "newMiniQuest").setStyle(getStyle())
                    .append(": ");
        }
        else{
            resultText = newTranslate(rootKey + dirFunctional + "newQuest").setStyle(getStyle())
                    .append(": ");
        }

        if(WTS.checkTranslationExist(keyQuestName, valQuestName)) {
            resultText.append(newTranslate(keyQuestName).setStyle(getSibling(0).getStyle()));
        }
        else {
            resultText.append(getSibling(0));
        }

        resultText = setToPressShift(resultText, fullText);
    }

    @Override
    public boolean print() {
        clearChat();
        return super.print();
    }
}
